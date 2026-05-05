# Web Automation

Automated UI tests using Playwright and TestNG.

## Prerequisites

- Java 11+
- Maven 3.6+
- Google Chrome/Chromium browser

## Configuration

Create a `.env` file in the project root:

```
BASE_URL=https://health-demo.digit.org/workbench-ui/employee/user/login
BROWSER=chromium
HEADLESS=false
USERNAME=LNMZ
PASSWORD=eGov@1234
COUNTRY=Nigeria
STATE=Bouenza
LGA=Loudima
WARD=Loudima gare
VILLAGE=Malela
AREA=Village12
```

## Running Tests

### 1. Local Execution (Full Suite)
```bash
mvn test
```

### 2. Run Specific Test Groups Locally
```bash
mvn test -Dtest.groups="payments-ui,workbench-ui"
```

### 3. Run Individual Test Class Locally
```bash
mvn test -Dtest.class="tests.LoginTest"
```

### 4. Run Individual Test Method Locally
```bash
mvn test -Dtest.class="tests.LoginTest" -Dtest.method="verifyUserCanLogin"
```

### 5. GitHub Actions Workflow (Manual Trigger)
1. Go to **Actions** tab in your repository
2. Select **Run Web Automation Tests**
3. Click **Run workflow**
4. Choose options:
   - **Test groups**: Comma-separated (e.g., `payments-ui,workbench-ui`)
   - **Test class**: Specific class (e.g., `tests.LoginTest`)
   - **Test method**: Specific method (e.g., `verifyUserCanLogin`)
   - **Environment**: UAT or DEMO

### 6. Trigger via Repository Dispatch (External)
```bash
curl -X POST https://api.github.com/repos/LataNaik-eGov/Web-Automation/dispatches \
  -H "Authorization: token YOUR_GITHUB_TOKEN" \
  -H "Accept: application/vnd.github.v3+json" \
  -d '{"event_type":"trigger-tests"}'
```

## GitHub Secrets Setup

Add these secrets in your repository settings (**Settings → Secrets and variables → Actions**):

### For DEMO environment:
- `DEMO_BASE_URL`
- `DEMO_USERNAME`
- `DEMO_PASSWORD`
- `DEMO_COUNTRY`
- `DEMO_STATE`
- `DEMO_LGA`
- `DEMO_WARD`
- `DEMO_VILLAGE`
- `DEMO_AREA`

### For UAT environment:
- `UAT_BASE_URL`
- `UAT_USERNAME`
- `UAT_PASSWORD`
- `UAT_COUNTRY`
- `UAT_STATE`
- `UAT_LGA`
- `UAT_WARD`
- `UAT_VILLAGE`
- `UAT_AREA`

## Test Groups

- `payments-ui`: Payment-related tests
- `workbench-ui`: Workbench-related tests
- `common`: Common tests (login, navigation)
- `regression`: Regression test suite
