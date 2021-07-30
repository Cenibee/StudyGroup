package com.cenibee.project.studygroup;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;

public abstract class BaseMockTest {

    @BeforeEach
    public void openMocks() {
        MockitoAnnotations.openMocks(this);
    }

}
