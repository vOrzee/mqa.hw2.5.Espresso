package ru.kkuzmichev.simpleappforespresso;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.content.Intent;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;

@RunWith(AllureAndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before // Выполняется перед тестами
    public void registerIdlingResources() { //Подключаемся к “счетчику”
        IdlingRegistry.getInstance().register(EspressoIdlingResources.idlingResource);
    }

    @Test
    public void testContentInHomeFragment() {
        ViewInteraction mainText = onView(
                withId(R.id.text_home)
        );
        mainText.check(
                matches(withText("This is home fragment"))
        );
    }

    @Test
    public void testPropertyIntent() {
        ViewInteraction overflowMenuButton = onView(withContentDescription("More options"));
        overflowMenuButton.perform(click());

        ViewInteraction materialTextView = onView(withText("Settings"));

        Intents.init();
        materialTextView.perform(click());
        intended(hasData("https://google.com"));
        intended(hasAction(Intent.ACTION_VIEW));
        Intents.release();
    }

    @Test
    public void testFoundItem() {
        ViewInteraction appCompatImageButton = onView(withContentDescription("Open navigation drawer"));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(withId(R.id.nav_gallery));
        navigationMenuItemView.perform(click());

        onView(allOf(
                isDescendantOfA(withId(R.id.recycle_view)),
                withText("7")
        )).check(matches(isDisplayed()));
    }

    @After // Выполняется после тестов
    public void unregisterIdlingResources() { //Отключаемся от “счетчика”
        IdlingRegistry.getInstance().unregister(EspressoIdlingResources.idlingResource);
    }
}
