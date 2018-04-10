package com.yahoo.inmind.testing.middleware;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by oscarr on 2/15/16.
 */
// Runs all unit tests.
@RunWith(Suite.class)
@Suite.SuiteClasses({
        RuleValidationCalendarTest.class,
        //WeatherTest.class,
        //NeilTest.class,
})
public class MiddlewareTestSuite{
}
