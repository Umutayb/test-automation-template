
# 🥒 Test Automation Template

A unified, multi-platform test automation framework built on **Java** and **Cucumber**. This framework utilizes the power of the **[pickleib](https://github.com/Umutayb/pickleib)** library to support Web, Mobile (Appium), Desktop (Appium), and API testing within a single codebase.

It is designed to be **developer-friendly** (Java POJOs) and **tester-friendly** (Low-Code JSON), allowing teams to scale automation rapidly.

---

## 🚀 Key Features

* **Multi-Platform Support:**
    * **Web:** Selenium WebDriver (Chrome, Firefox, etc.)
    * **Mobile:** Appium (Android/iOS)
    * **Desktop:** WinAppDriver (Windows Applications) & XCUI (MacOS Applications)
    * **API:** `wasapi` integration
* **Dual Repository Design:** Define page elements via standard Java classes **OR** a simple JSON file.
* **Smart Interactions:** Built-in handling for flaky elements, scrolling, and wait conditions via `Pickleib`.
* **State Management:** Thread-safe `ContextStore` to pass data (generated users, IDs, tokens) between test steps.
* **Integrations:**
    * **Slack:** Automated test run notifications with status and screenshots.
    * **Email:** Real-time email fetching and verification (Gmail POP3).

---

## 🏗️ Architecture & Design

The framework follows a modular architecture designed for maintainability and separation of concerns.

### The "Dual Design" Capability


This framework offers a unique **Hybrid Element Management** system. You can store your page locators in two distinct ways, but the **Cucumber steps remain exactly the same** for both. The framework's `ElementAcquisition` class handles the retrieval in the background.

#### 1. The Low-Code Design (JSON)
*Best for: Rapid prototyping, non-technical contributors, and centralizing selectors.*

**Step 1: Define Elements in JSON**
Pages and elements are defined in `src/test/resources/page-repository.json`. No Java classes are required.

```json
{
  "name": "LoginPage",
  "elements": [
    {
      "elementName": "usernameInput",
      "selectors": { "web": [{ "css": "#userName" }] }
    }
  ]
}

```

**Step 2: Configure CommonSteps**
To enable the JSON acquisition, `CommonSteps` must extend `PageJsonDesign` and parse the JSON file.

```java
public class CommonSteps extends PageJsonDesign {

    public CommonSteps() {
        super(
                FileUtilities.Json.parseJsonFile("src/test/resources/page-repository.json"),
                Hooks.initialiseAppiumDriver,
                Hooks.initialiseBrowser
        );
    }
}

```

#### 2. The Classic POM Design (Java)

*Best for: Complex logic and custom helper methods.*

Pages are Java classes extending `PickleibPageObject`. Elements are fields decorated with `@FindBy`.

**Step 1: Create the Page Class**

```java
public class LoginPage extends PickleibPageObject {
    @FindBy(css = "#userName")
    public WebElement usernameInput;
}

```

**Step 2: Register in ObjectRepository**
⚠️ **Crucial Step:** You must declare your new page class in `src/test/java/common/ObjectRepository.java` for the framework to detect it.

```java
public class ObjectRepository implements PageObjectRepository {
    // ... other pages
    LoginPage loginPage; // <--- Add this line
}

```

**Step 3: Configure CommonSteps**
To enable the Java POM acquisition, the `CommonSteps` class must extend `PageObjectDesign` and register the `ObjectRepository` class.

```java
public class CommonSteps extends PageObjectDesign {

    public CommonSteps() {
        super(
                ObjectRepository.class,
                Hooks.initialiseAppiumDriver,
                Hooks.initialiseBrowser
        );
    }
}

```

#### 3. Unified Execution

Regardless of the design chosen above, the Gherkin step is identical:

```gherkin
Given Fill input usernameInput on the LoginPage with text: "myUser"

```

---

## 📋 Prerequisites

Ensure you have the following installed:

* **Java JDK 17**
* **Maven** (3.6+)
* **Google Chrome** (for Web tests)
* **Appium Server** (required for Mobile or Desktop tests)

---

## ⚙️ Configuration

### Page Repository

If using the Low-Code approach, ensure your selectors are updated in:
`src/test/resources/page-repository.json`

---

## 🏃 Running Tests

Tests are executed via Maven using Cucumber Tags to filter the scope.

### 1. Web UI Tests

```bash
mvn clean test -Dcucumber.filter.tags="@Web-UI"

```

### 2. Mobile/Desktop Tests

*Ensure Appium server is running before execution for mobile/desktop tests.*

```bash
mvn clean test -Dcucumber.filter.tags="@Mobile-UI"
# OR for Windows Desktop Apps
mvn clean test -Dcucumber.filter.tags="@Desktop-UI"

```

### 3. API Tests

```bash
mvn clean test -Dcucumber.filter.tags="@API"

```

### 4. Run Specific Scenario

```bash
mvn clean test -Dcucumber.filter.tags="@SCN-001"

```

---

## ⚡ Quickstart Guide for New Engineers

If you are adding a new test for a web page, follow the **Low-Code** approach to get started in under 5 minutes.

### Step 1: Inspect the Element

Open your browser, inspect the element (e.g., a "Submit" button), and copy its CSS selector or ID.

### Step 2: Update JSON Repository

Open `src/test/resources/page-repository.json`. Add the page (if it doesn't exist) and the element.

```json
{
  "name": "ContactPage",
  "elements": [
    {
      "elementName": "submitButton",
      "selectors": { "web": [{ "css": ".btn-submit-form" }] }
    }
  ]
}

```

### Step 3: Write the Test

Open a `.feature` file and write your step. You do **not** need to write any Java code.

```gherkin
Scenario: Submit contact form
    Given Navigate to the test page
    # The framework matches "submitButton" and "ContactPage" from the JSON
    When Click the submitButton on the ContactPage

```

---

## 📊 Reporting

* **Local Reports:** Generated at `target/reports/Cucumber.json`.
* **Slack:** If configured, a summary is posted to Slack with pass/fail status and screenshots of failures.

---

## 🤝 Contribution

1. **Branch:** Create a feature branch (`git checkout -b feature/AmazingFeature`).
2. **Code:** Add your Feature file and update `page-repository.json` (or add Java Page Objects).
3. **Commit:** `git commit -m 'Add some AmazingFeature'`
4. **Push:** `git push origin feature/AmazingFeature`
5. **Open a Pull Request.**
