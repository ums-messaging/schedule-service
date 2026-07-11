package com.ums.schedule.application.message.email;

import com.ums.schedule.application.ums.email.message.EmailMessageCreateService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmailMessageCreateServiceTest {

    @InjectMocks private EmailMessageCreateService messageService;
}