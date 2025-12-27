# 🥒 Pickleib Automation Template

[![Pickleib](https://img.shields.io/maven-central/v/io.github.umutayb/pickleib?color=brightgreen&label=pickleib)](https://mvnrepository.com/artifact/io.github.umutayb/pickleib/latest)

This is a **ready-to-use test automation template** built on top of **[Pickleib](https://github.com/Umutayb/pickleib)**.

It provides a pre-configured environment for **Web, Mobile, Desktop, and API testing** using a single, unified set of Gherkin steps. By extending `PickleibSteps`, this template allows you to write interaction-agnostic scenarios that work across platforms without writing custom Java code for every interaction.



---

## 🚀 Key Capabilities

* **⚡ Zero-Code Interaction:** most of the standard interactions (clicks, fills, checks) are already defined in `CommonSteps`.
* **📂 JSON Page Repository:** Manage your selectors in a `page-repository.json` file. No need to create Java Page classes unless you want to.
* **📱 Platform Agnostic:** The same step `Click "loginButton"` works for Selenium (Web) and Appium (Mobile/Desktop).
* **🧠 Context Awareness:** Built-in `ContextStore` to pass data between steps (e.g., save a User ID in step 1, use it in step 5).
* **📜 Listed Elements:** specialized support for finding elements inside lists (e.g., "Find the row containing 'John' and click 'Edit'").

---

## 📖 Essential Steps (`CommonSteps`)

While `CommonSteps` contains a massive library of interactions, these **5 steps** are sufficient to automate most of standard use cases.

| Step Pattern | Description |
| :--- | :--- |
| `Navigate to url: {}` | **Navigate:** Directs the driver to a specific URL. |
| `^(?:Click\|Tap) the (\w+) on the (\w+)$` | **Click:** Interactions with a button or link defined in your Page Object/JSON. |
| `^Fill input (\w+) on the (\w+) with (?:(un-verified\|verified) )?text: (.+?(?:\s+.+?)*)$` | **Type:** Clears and fills a specific input field. |
| `^Verify the text of (\w+) on the (\w+) to be: (.+?(?:\s+.+?)*)$` | **Assert Text:** Validates that an element's text matches your expectation. |
| `^Verify presence of element (\w+) on the (\w+)$` | **Assert Display:** Validates that an element is visible on the screen. |

> ℹ️ **Note:** See the **Step Library** section for the full list of available steps and advanced interactions.

---

## 🏗️ How to Define Elements

This template defaults to the **Low-Code JSON** approach.
Open `src/test/resources/page-repository.json` to define your pages.

```json
{
  "pages": [
    {
      "name": "PracticeFormPage",
      "platform": "web",
      "elements": [
        {
          "elementName": "firstNameInput",
          "selectors": { "web": [{ "id": "firstName" }] }
        },
        {
          "elementName": "submitButton",
          "selectors": { "web": [{ "css": "#submit" }] }
        }
      ]
    }
  ]
}
```

---

## 🏃 Usage Example

Below is a real-world example from the included **DemoQA** feature. It demonstrates filling a complex form and validating a data grid results table.

```gherkin
@Web-UI
Scenario: DemoQA form interactions
  # Navigation
  Given Navigate to the test page
  
  # List Interaction (Clicking an item from a menu list)
  When Click listed element "Forms" from "categoryCards" list on the "LandingPage"
  And Click listed element "Practice Form" from "tools" list on the "ToolsPage"

  # Batch Form Filling
  And Fill form input on the "PracticeFormPage"
    | Input Element  | Input                |
    | firstNameInput | Testing              |
    | lastNameInput  | isGreat              |
    | userEmailInput | pickleib@email.com   |
    | userNumber     | 0655500001           |

  # Complex Interactions
  And Upload file on input "uploadPictureButton" on the "PracticeFormPage" with file: "UPLOAD-src/test/resources/files/profile-picture.jpg"
  And Click the "submitButton" on the "PracticeFormPage"

  # Intelligent Validation (Find a row by 'Student Name', verify it contains 'Umut Bora')
  Then Select listed element containing partial text "Student Name" from the "resultModelDataRows" on the "PracticeFormPage" and verify its text contains "Testing isGreat"
```

---

## ⚙️ Configuration

1.  **Driver Config:** Edit `src/test/resources/pickleib.properties` to set browser types, timeouts, and headless modes.
2.  **Steps Config:** The `CommonSteps.java` constructor controls where the definitions are loaded from:

```java
import pickleib.utilities.steps.PickleibSteps;
import org.openqa.selenium.WebElement;

public class CommonSteps extends PickleibSteps {

  public CommonSteps() {
    // Point to your JSON file
    super("src/test/resources/page-repository.json");
  }

  @When("I click the {string} on the {string} page")
  public void clickTheButton(String buttonName, String pageName) {
    log.info("Clicking the " + buttonName + " on the " + pageName);

    // Acquire element dynamically from the JSON definition
    WebElement button = getElementRepository().acquireElementFromPage(buttonName, pageName);

    // Perform interaction
    getInteractions(button).clickElement(button);
  }
}
```

---

## 💻 Execution

Run tests using Maven. The template uses tags to filter scenarios.

```bash
# Run Web Tests
mvn clean test -Dcucumber.filter.tags="@Web-UI"

# Run Mobile Tests (Requires Appium running)
mvn clean test -Dcucumber.filter.tags="@Mobile-UI" -Ddevice=Pixel_Emulator

# Run a specific feature
mvn clean test -Dcucumber.filter.tags="@DemoQA"
```

## 📖 The Step Library (`CommonSteps`)

This template comes with a massive library of pre-defined Gherkin steps located in `steps.CommonSteps`. You can use these immediately in your `.feature` files.

### 🧭 Navigation & Window Management

| Step Pattern | Description |
| :--- | :--- |
| `^Set default platform as (appium\|selenium)$` | Switches the driver context. |
| `Navigate to url: {}` | Direct navigation. |
| `^Navigate to the (acceptance\|test\|dev) page$` | Navigates to environment URLs from properties. |
| `Go to the {} page` | Appends a path to the base URL. |
| `Refresh the page` | Reloads the current page. |
| `^Navigate browser (BACKWARDS\|FORWARDS)$` | History navigation. |
| `Switch to the next tab` | Focuses the next browser tab. |
| `Switch back to the parent tab` | Returns focus to the parent handle. |
| `Switch to the tab with handle: {}` | Focus specific tab by handle. |
| `Switch to the tab number {}` | Focus specific tab by index. |
| `Switch to the next active window` | Focus next window. |
| `Set window width & height as {} & {}` | Resize viewport. |
| `Save current url to context` | Stores URL in `ContextStore`. |

### 🖱️ Interactions (Click, Fill, Scroll)

| Step Pattern | Description |
| :--- | :--- |
| `^(?:Click\|Tap) the (\w+) on the (\w+)$` | Standard click on element/page. |
| `^If enabled, (?:click\|tap) the (\w+) on the (\w+)$` | Conditional click (safe). |
| `^If present, click the (\w+) on the (\w+)$` | Click only if element exists. |
| `^Click towards the (\w+) on the (\w+)$` | Click offset/towards element. |
| `Click button with {} css locator` | Click using raw CSS. |
| `^(?:Click\|Tap) button with (.+?(?:\s+.+?)*) text(?: using (Mobile\|Web) driver)?$` | Click by text content. |
| `^Fill input (\w+) on the (\w+) with (?:(un-verified\|verified) )?text: (.+?(?:\s+.+?)*)$` | Fills input (optionally verified). |
| `Upload file on input {} on the {} with file: {}` | File upload via local path. |
| `Fill iFrame element {} of {} on the {} with text: {}` | Input interaction inside an iFrame. |
| `Click i-frame element {} in {} on the {}` | Click interaction inside an iFrame. |
| `^(?:Scroll\|Swipe) (up\|down\|left\|right) using (Mobile\|Web) driver$` | Directional scroll. |
| `^(?:Scroll\|Swipe) until element with exact text (.+?(?:\s+.+?)*) is found using (Web\|Mobile) driver$` | Scroll until text visible. |
| `Center the {} on the {}` | Scrolls element into viewport center. |

### 📋 List & Collection Handling

| Step Pattern | Description |
| :--- | :--- |
| `^(?:Click\|Tap) listed element (.+?(?:\s+.+?)*) from (\w+) list on the (\w+)$` | Click element within a list. |
| `^Fill listed input (\w+) from (\w+) list on the (\w+) with text: (.+?(?:\s+.+?)*)$` | Fill input within a list. |
| `^(?:Scroll\|Swipe) until listed (.+?(?:\s+.+?)*) element from (\w+) list is found on the (\w+)$` | Scroll within a specific list container. |
| `^Select listed element containing partial text (.+?(?:\s+.+?)*) from the (\w+) on the (\w+) and verify its text contains (.+?(?:\s+.+?)*)$` | Find row by partial text & verify. |
| `Perform text verification for listed elements of {} list on the {} contains {}` | Iterates list verifying text. |
| `^Click listed attribute element that has (.+?(?:\s+.+?)*) value for its (\w+) attribute from (\w+) list on the (\w+)$` | Click list item by attribute match. |

### ✅ Verification & Assertions



| Step Pattern | Description |
| :--- | :--- |
| `^Verify the text of (\w+) on the (\w+) to be: (.+?(?:\s+.+?)*)$` | Exact text verification. |
| `^Verify the text of (\w+) on the (\w+) contains: (.+?(?:\s+.+?)*)$` | Partial text verification. |
| `^Verify presence of element (\w+) on the (\w+)$` | Asserts displayed. |
| `^Verify absence of element (\w+) on the (\w+)(?: using (Mobile\|Web) driver)?$` | Asserts not displayed. |
| `^Verify that element (\w+) on the (\w+) is in (\w+) state$` | Check enabled/disabled/selected. |
| `Verify the url contains with the text {}` | URL validation. |
| `Assert that value of {} is equal to {}` | String equality assertion. |
| `Assert that value of {} is contains {}` | String contains assertion. |
| `Wait until element (\w+) on the (\w+) has (.+?(?:\s+.+?)*) value for its (\w+) attribute` | Wait for attribute change. |

### 🧠 Context, Cookies & Utilities

| Step Pattern | Description |
| :--- | :--- |
| `^Wait (\d+) seconds$` | Hard wait. |
| `Update context {} -> {}` | Update `ContextStore`. |
| `Save context value from {} context key to {}` | Copy context value. |
| `Add the following values to LocalStorage:` | DataTable to LocalStorage. |
| `Add the following cookies:` | DataTable to Cookies. |
| `Delete cookies` | Clear all cookies. |
| `Execute JS command: {}` | Run raw JavaScript. |
| `Execute script {string} on element with text {string}` | JS on specific element. |
| `Execute mobile editor command: {}` | Mobile editor actions (search, go, etc). |

