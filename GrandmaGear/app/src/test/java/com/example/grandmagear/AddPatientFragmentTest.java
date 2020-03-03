package com.example.grandmagear;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;

import com.google.firebase.firestore.auth.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.manipulation.Ordering;

import static org.junit.Assert.*;

public class AddPatientFragmentTest {
    private UserActivity mUserActivity = null;
    @Before
    public void setUp() throws Exception {
        mUserActivity = new UserActivity();
        PatientDevice patientDevice = new PatientDevice("ok", "this",
                "is", "working");
    }

    @Test
    public void testLaunch(){

    }

    @After
    public void tearDown() throws Exception {
    }
}