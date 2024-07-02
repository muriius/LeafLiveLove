package com.example.androidtest6;

import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

import android.view.View;
import android.widget.TextView;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;


// ...


@RunWith(AndroidJUnit4.class)
public class EspressoTests {

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testUpdateButtonShowsSnackbar(){
        // Click on the update button
        onView(withId(R.id.updateButton)).perform(click());

        onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches(isDisplayed()));

        // Check if the Snackbar with text "Werte aktualisiert" is displayed
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("Werte aktualisiert")));

        // Check if the update button is clickable
        onView(withId(R.id.updateButton)).check(matches(isClickable()));


        }


    @Test
    public void testSnackbarDisappears() throws InterruptedException {
    onView(withId(R.id.updateButton)).perform(click());
    Thread.sleep(5000);
    onView(withId(com.google.android.material.R.id.snackbar_text)).check(doesNotExist());
    }


    @Test
    public void testLoadingIsDisplayedAtStart() {
        // Check if "Temperature Loading..." is displayed at the start
        onView(withId(R.id.temperatureValue)).check(matches(withText("Temperature Loading...")));
        onView(withId(R.id.temperatureValue)).check(matches(isDisplayed()));

        // Check if "Humidity Loading..." is displayed at the start
        onView(withId(R.id.humidityValue)).check(matches(withText("Humidity Loading...")));
        onView(withId(R.id.humidityValue)).check(matches(isDisplayed()));

        // Check if "Moisture Loading..." is displayed at the start
        onView(withId(R.id.moistureValue)).check(matches(withText("Moisture Loading...")));
        onView(withId(R.id.moistureValue)).check(matches(isDisplayed()));

    }

    @Test
    public void testValuesContainOnlyAllowedSymbols() throws InterruptedException {
        onView(withId(R.id.updateButton)).perform(click());
/*
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

 */
        // Define a Matcher that matches Strings that only contain digits, °C, % and the word "Temperatur: "
        Matcher<String> onlyAllowedSymbolsTemperature = Matchers.matchesPattern("^Temperatur: [0-9°C]+$");

        // Check if the temperature value contains only allowed symbols
        onView(withId(R.id.temperatureValue)).check(matches(withText(onlyAllowedSymbolsTemperature)));

        // Define a Matcher that matches Strings that only contain digits, % and the word "Luftfeuchtigkeit: "
        Matcher<String> onlyAllowedSymbolsHumidity = Matchers.matchesPattern("^Luftfeuchtigkeit: [0-9%]+$");

        // Check if the humidity value contains only allowed symbols
        onView(withId(R.id.humidityValue)).check(matches(withText(onlyAllowedSymbolsHumidity)));

        // add the same for Luftfeuchtigkeit:
        // Define a Matcher that matches Strings that only contain digits, % and the word "Wasser: "
        Matcher<String> onlyAllowedSymbolsMoisture = Matchers.matchesPattern("^Wasser: [0-9%]+$");
        onView(withId(R.id.moistureValue)).check(matches(withText(onlyAllowedSymbolsMoisture)));

    }

/*
    @Test
    public void testValuesContainOnlyAllowedSymbols() throws InterruptedException {

        Thread.sleep(5000);
        // Define a Matcher that matches Strings that only contain digits, °C, and %
        Matcher<String> onlyAllowedSymbols = Matchers.matchesPattern("^[0-9°C%]+$");

        // Check if the temperature value contains only allowed symbols
        onView(withId(R.id.temperatureValue)).check(matches(withText(allOf(containsString("°C"), onlyAllowedSymbols))));

        // Check if the humidity value contains only allowed symbols
        onView(withId(R.id.humidityValue)).check(matches(withText(allOf(containsString("%"), onlyAllowedSymbols))));
    }

 */

// ...
/*
    @Test
    public void testValuesContainOnlyAllowedSymbols() {
        // Define a Matcher that matches Strings that only contain digits, °C, and %
        Matcher<String> onlyAllowedSymbols = Matchers.matchesPattern("^[0-9°C%]+$");

        // Check if the temperature value contains only allowed symbols
        onView(withId(R.id.temperatureValue)).check(matches(withText(allOf(containsString("°C"), new BoundedMatcher<View, TextView>(TextView.class) {
            @Override
            protected boolean matchesSafely(TextView item) {
                String text = item.getText().toString();
                String[] parts = text.split(": ");
                if (parts.length == 2) {
                    return onlyAllowedSymbols.matches(parts[1]);
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with text value containing symbols: " + onlyAllowedSymbols.toString());
            }
        }))));

        // Check if the humidity value contains only allowed symbols
        onView(withId(R.id.humidityValue)).check(matches(withText(allOf(containsString("%"), onlyAllowedSymbols))));
    }

        // Check if the humidity value contains only allowed symbols
        onView(withId(R.id.humidityValue)).check(matches(withText(allOf(containsString("%"), onlyAllowedSymbols))));
}


*/
}