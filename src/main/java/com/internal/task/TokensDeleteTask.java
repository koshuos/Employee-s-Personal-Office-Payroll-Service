package com.internal.task;


import com.internal.service.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Date;

@Service
@Transactional
public class TokensDeleteTask {

    @Autowired
    VerificationToken verificationToken;

    //  @Scheduled(fixedDelay = 5000)
    @Scheduled(cron = "0 0 0/12 * * ?")
    public void deleteToken() {
        Date now = Date.from(Instant.now());
        verificationToken.deleteExpiredPasswordTokenUser(now);
    }
}
