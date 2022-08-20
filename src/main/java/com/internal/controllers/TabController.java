package com.internal.controllers;

import com.internal.async.AsynchronousService;
import com.internal.model.*;
import com.internal.service.DataDao;
import com.linuxense.javadbf.DBFException;
import com.linuxense.javadbf.DBFReader;
import com.linuxense.javadbf.DBFUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
public class TabController {

    @Autowired
    private DataDao dao;
    @Autowired
    private AsynchronousService asynchronousService;

    @RequestMapping(path = "/gettublist/{id}", method = RequestMethod.GET)
    public List<Tab_show> getReportsSud(@PathVariable Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Users userfromName = dao.getUserfromName(currentPrincipalName);

        List<Tab_show> tabData = dao.getTabData(id,
                Integer.valueOf(dao.getEmployeesFromId(userfromName.getUserid()).getTabelNomer()));

        replaselist(tabData, "Відсоток оплати", "відсоток");
        replaselist(tabData, "Оклад(Тариф)", "оклад");
        replaselist(tabData, "Оклад/Тариф", "оклад");
        replaselist(tabData, "Сума", "сума");
        remove_zerro(tabData);    // убираем строки с нулевой оплатой


        //Поиск больничных от государства
        double lik = 0.0;
        for (Tab_show item : tabData) {
            int index = item.getName().lastIndexOf("Оплата лікарняних листів");

            double v = 0.0;
            if (!(index == -1)) {
                int index2 = item.getName().lastIndexOf("перші 5 днів");
                if (index2 == -1) {
                    String[] s = item.getValue().split(" ");
                    String s1 = s[0].replace(",", ".").replace(" ", "").replace("\u00A0", "");
                    try {
                        v = Double.parseDouble(s1);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }

            lik = lik + v;
        }


        if (lik != 0.0) {

            double vsego_narahovano = 0.0;
            double vsego_utrumano = 0.0;
            double zalushok_na_pohatok_mis = 0.0;
            double vsego_viplasheno = 0.0;

            for (Tab_show item : tabData) {
                if (item.getName().equals("ВСЬОГО НАРАХОВАНО")) {
                    vsego_narahovano = Double.parseDouble(item.getValue().replace(",", ".").replace(" ", "").replace("\u00A0", ""));
                }
                if (item.getName().equals("ВСЬОГО УТРИМАНО")) {
                    vsego_utrumano = Double.parseDouble(item.getValue().replace(",", ".").replace(" ", "").replace("\u00A0", ""));
                }
                if (item.getName().equals("Залишок за підприємством на початок місяця")) {
                    zalushok_na_pohatok_mis = Double.parseDouble(item.getValue().replace(",", ".").replace(" ", "").replace("\u00A0", ""));
                }
                if (item.getName().equals("ВСЬОГО ВИПЛАЧЕНО")) {
                    vsego_viplasheno = Double.parseDouble(item.getValue().replace(",", ".").replace(" ", "").replace("\u00A0", ""));
                }
            }

            // System.out.println(lik);
            int size = tabData.size();

            Tab_show tab_show = tabData.get(size - 1);

            String val = tab_show.getValue().replace(",", ".").replace(" ", "").replace("\u00A0", "");

            double lik_n = lik * 0.195;
            double fss = (double) Math.round((lik - lik_n) * 100) / 100;

            tabData.remove(size - 1);

            double nakartu = (double) Math.round(((vsego_narahovano + zalushok_na_pohatok_mis) - vsego_utrumano - vsego_viplasheno - fss) * 100) / 100;

            tab_show.setValue(String.valueOf(nakartu));

            tabData.add(tab_show);

            Tab_show tab_show1 = new Tab_show("Залишок за ФСС", String.valueOf(fss), 408, 3);
            tabData.add(tab_show1);

        }

        //
        return tabData;
    }

    private void replaselist(List<Tab_show> tabData, String target, String replase) {
        for (ListIterator<Tab_show> iter = tabData.listIterator(); iter.hasNext(); ) {
            Tab_show item = iter.next();
            int index = item.getName().lastIndexOf(target);
            if (!(index == -1)) {
                Tab_show replacement = new Tab_show(item.getName(), item.getValue(), item.getKod(), item.getType());
                replacement.setName(item.getName().replace(target, replase));
                // iter.remove();
                iter.set(replacement);
                // iter.add(replacement);
            }
        }
    }

    private void remove_zerro(List<Tab_show> tabData) {
        for (ListIterator<Tab_show> iter = tabData.listIterator(); iter.hasNext(); ) {
            Tab_show item = iter.next();

            String[] s = item.getValue().split(" ");

            if (s[0].equals("0")) {
                iter.remove();
            }

        }
    }

    @RequestMapping(path = "files", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse fileupload(@RequestBody FileDto fileDto){

        List<TabulagramData> data_list = new ArrayList<>();
        List<TabulagramSpr> spr_list = new ArrayList<>();

        Period periodfromId = fileDto.getPeriod();

        String partSeparator = ",";

        String data = fileDto.getData_data().split(partSeparator)[1];
        byte[] data_b = Base64.getDecoder().decode(data.getBytes(StandardCharsets.UTF_8));
        String spr = fileDto.getData_spr().split(partSeparator)[1];
        byte[] spr_b = Base64.getDecoder().decode(spr.getBytes(StandardCharsets.UTF_8));


        DBFReader reader = null;
        try {
            reader = new DBFReader(new ByteArrayInputStream(spr_b), Charset.forName("Windows-1251"));

            // Проверка на структуру файла
            if (!(reader.getField(0).getName().equals("ATTR_ID") &&
                    reader.getField(1).getName().equals("ATTR_NAME") &&
                    reader.getField(2).getName().equals("ATTR_DOP"))) {

                return new GenericResponse("spr_fail");
            }

            Object[] rowObjects;
            while ((rowObjects = reader.nextRecord()) != null) {
                TabulagramSpr tabulagramSpr = new TabulagramSpr();
                tabulagramSpr.setPeriod(periodfromId);
                tabulagramSpr.setKod(Integer.valueOf(rowObjects[0].toString()));
                String str = rowObjects[1].toString();
                if (str.equals("Военный сбор")) {
                    str = "Вiйсковий збiр";
                }
                if (str.equals("НДФЛ")) {
                    str = "ПДФО";
                }

                str = str.replace("показатели:", ":");
                str = str.replace("Процент оплаты", "Відсоток оплати");
                str = str.replace("Сумма", " Сума");

                tabulagramSpr.setStr(str);
                if (rowObjects[2] != null) {
                    String s = rowObjects[2].toString();
                    if (!s.equals("")) {
                        tabulagramSpr.setType(Integer.valueOf(s));
                    }

                }
                spr_list.add(tabulagramSpr);
            }
        } catch (DBFException e) {
            e.printStackTrace();
        } finally {
            DBFUtils.close(reader);
        }

        DBFReader reader2 = null;
        try {
            reader2 = new DBFReader(new ByteArrayInputStream(data_b), Charset.forName("Windows-1251"));

            // Проверка на структуру файла
            if (!(reader2.getField(0).getName().equals("TAB_NUMB") &&
                    reader2.getField(1).getName().equals("ATTR_ID") &&
                    reader2.getField(2).getName().equals("ZNACH1"))) {

                return new GenericResponse("data_fail");
            }


            Object[] rowObjects;
            while ((rowObjects = reader2.nextRecord()) != null) {
                TabulagramData tabulagramData = new TabulagramData();
                tabulagramData.setPeriod(periodfromId);
                tabulagramData.setTabN(Integer.valueOf(rowObjects[0].toString()));
                tabulagramData.setKod(Integer.valueOf(rowObjects[1].toString()));
                tabulagramData.setData(String.valueOf(rowObjects[2]));
                data_list.add(tabulagramData);
            }
        } catch (DBFException e) {
            e.printStackTrace();
        } finally {
            DBFUtils.close(reader2);
        }

        dao.truncateLastpay(periodfromId.getId());

        dao.saveTabulagramSpr(spr_list);

        dao.saveTabulagramData(data_list, fileDto.getEmail());

        asynchronousService.sendEmais();

        return new GenericResponse("success");
    }

}
