@Desktop-UI
Feature: Windows Application Demo
Demonstrating UI capabilities of Pickleib

  Background: Populate context
    * Update context username -> spriteCloud
    * Update context password -> Secret-123

  Scenario: New user product flow #1
    * Click the registerButton on the LoginFrame
    * Update context username -> newInventoryUser#96
    * Fill input loginInput on the LoginFrame with text: CONTEXT-username
    * Fill input passwordInput on the LoginFrame with text: CONTEXT-password
    * Click the submitButton on the LoginFrame
    * Click the OKButton on the LoginFrame
    * Click the loginButton on the LoginFrame
    * Fill input loginInput on the LoginFrame with text: CONTEXT-username
    * Fill input passwordInput on the LoginFrame with text: CONTEXT-password
    * Click the submitButton on the LoginFrame
    * Switch to the next active window
    * Click the categoriesButton on the InventoryManagementFrame
    * Click the addButton on the InventoryManagementFrame
    * Fill input categoryInput on the Categories with text: Groceries
    * Click the saveButton on the InventoryManagementFrame
    * Click the productsButton on the InventoryManagementFrame
    * Click the addButton on the InventoryManagementFrame
    * Fill input nameInput on the Products with text: Cucumber
    * Fill input priceInput on the Products with text: 10
    * Fill input stockInput on the Products with text: 99
    * Fill input unitInput on the Products with text: 10
    * Fill input groceriesInput on the Products with text: Groceries
    * Click the saveButton on the InventoryManagementFrame
    * Click the addToCart on the Products
    * Click the OKButton on the InventoryManagementFrame
    * Click the salesButton on the InventoryManagementFrame
    * Click the checkOutButton on the Sales
    * Fill input paymentAmountInput on the Sales with text: 50
    * Click the paymentButton on the Sales
    * Click the transactionButton on the InventoryManagementFrame
    * Save transaction to context
    * Verify save transaction against the database