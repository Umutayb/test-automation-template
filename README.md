# Quickstart Library

### How To Get Started:

First, the library should be exported into an empty automation project by using:
```shell
mvn clean package -DbuildDirectory=directory/to/project/lib
```
For instance:
```shell
mvn clean package -DbuildDirectory=/Users/Umut/Github/Web-Automation-Sample-Cucumber/lib
```
There, the imported jar file should be added as a dependency in pom.xml file of that project:
```xml
<!-- Framework -->
<dependency>
    <groupId>bora</groupId>
    <artifactId>POM-Framework-0.0.2.jar</artifactId>
    <version>0.0.2</version>
    <systemPath>${project.basedir}/lib/POM-FRAMEWORK-0.0.2.jar</systemPath>
    <scope>system</scope>
    <type>jar</type>
</dependency>
```
After updating your project, the quickstart library is ready to use.
___
### How To Use:

The quickstart library consists of many utility methods and a ready to use built in selenium grid infrastructure,
compatible with page object model design. The infrastructure allows easy initialization of elements by initializing them
within a constructor inside the **Utilities** class. In order to initialize elements inside a page class, all it takes is
to extend the **Utilities** class. This also extends the **Driver** class, allowing usage of driver inside page classes.

#### Step 1: Create a pages package
>Create page classes, add elements (use @FindBy annotation) & page methods. _**Remember** extending **Utilities** class,
> initializing all elements within the page class._
>````java
> public class HomePage extends Utilities {...}
>```` 

#### Step 2: Create a steps package
>Create page step classes, instantiate page classes, create step definitions & access page methods within these step
> definitions as:
> ````java
> public class HomePageSteps {
> 
>    HomePage homePage = new HomePage();
>
>    @Given("Click category card named {}")
>    public void clickCategoryCard(String cardName) {
>        homePage.clickCategoryCardNamed(cardName);
>    }
> }
> ````
>Set cucumber @Before & @After steps as:
> ````java
>    Initialize driverManager = new Initialize();
>
>    @Before
>    public void start(){driverManager.init();}
>
>    @After
>    public void kill(Scenario scenario){driverManager.kill(scenario);}
>````
> This will initialize the driver before each run, and kill it after each scenario is done. It will also
> capture a ss if the scenario fails, indicating scenario name and failed step info.

#### Step 3: Create a features package
>Create _**.feature**_ files, create your scenarios using the steps you have implemented in ***Step 2***.

Note: Your feature file must contain a @Web-UI tag to initialize the browser if it requires a web driver
#### Step 4: Execute your tests
>###### Selenium Grid needs to be running first, turn on Docker, then in project directory start Selenium Grid & Nodes by using the following command:
>````shell
>docker-compose up -d
>````
>Alternatively, set selenium.grid property to false in test properties, that way the tests will run locally
> ######
> ___
>The library allows the browser type to be designated on runtime, just pass:
> ````
> -Dbrowser=browserName
> ````
> For instance:
>````
> -Dbrowser=firefox
>````
>Chrome, Firefox, Opera, Edge & Safari are supported.
>In order to use this feature, please add the following plugin & property to your pom.xml:

```xml
    <properties>
        <browser>Chrome</browser>
        <name>${project.name}</name>
    </properties>
```    
___
```xml
    <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>properties-maven-plugin</artifactId>
        <version>1.0.0</version>
        <executions>
            <execution>
                <phase>generate-resources</phase>
                    <goals>
                        <goal>write-project-properties</goal>
                    </goals>
                    <configuration>
                        <outputFile>${project.build.outputDirectory}/properties-from-pom.properties</outputFile>
                    </configuration>
            </execution>
        </executions>
    </plugin>
```
#### Example execution command:
>In order to execute a specific feature file in a specific browser, add tags to the first line of your feature file & use:
>```shell
>mvn clean test -q -Dcucumber.filter.tags="@Registration and @TestEnv" -Dbrowser=chrome
>```
___
>#### To create a cucumber project:
>````shell
>mvn archetype:generate                      \
>"-DarchetypeGroupId=io.cucumber"           \
>"-DarchetypeArtifactId=cucumber-archetype" \
>"-DarchetypeVersion=6.10.4"               \
>"-DgroupId=hellocucumber"                  \
>"-DartifactId=hellocucumber"               \
>"-Dpackage=hellocucumber"                  \
>"-Dversion=1.0.0-SNAPSHOT"                 \
>"-DinteractiveMode=false"
>````
Uses:
[![](https://jitpack.io/v/Umutayb/Pickleib.svg)](https://jitpack.io/#Umutayb/Pickleib)
