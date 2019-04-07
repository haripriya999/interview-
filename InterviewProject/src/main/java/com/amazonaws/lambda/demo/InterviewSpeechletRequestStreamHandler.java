package com.amazonaws.lambda.demo;

import java.util.HashSet;
import java.util.Set;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

public class InterviewSpeechletRequestStreamHandler  extends SpeechletRequestStreamHandler {
    private static final Set<String> supportedApplicationIds;
    static {
        /*
         * This Id can be found on https://developer.amazon.com/edw/home.html#/ "Edit" the relevant
         * Alexa Skill and put the relevant Application Ids in this Set.
         */
        supportedApplicationIds = new HashSet<String>();
        supportedApplicationIds.add("amzn1.ask.skill.c72d9367-c9d8-494e-8d5d-e1c9e512e4f8");
    }

    public InterviewSpeechletRequestStreamHandler() {
        super(new InterviewSpeechlet(), supportedApplicationIds);
    }
}