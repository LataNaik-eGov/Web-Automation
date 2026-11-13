---
marp: true
theme: default
paginate: true
style: |
  section {
    font-family: 'Segoe UI', Arial, sans-serif;
    font-size: 28px;
  }
  h1 { color: #0057A8; font-size: 42px; }
  h2 { color: #0057A8; border-bottom: 2px solid #0057A8; padding-bottom: 8px; }
  h3 { color: #444; }
  .highlight { color: #0057A8; font-weight: bold; }
  table { width: 100%; border-collapse: collapse; font-size: 22px; }
  th { background: #0057A8; color: white; padding: 8px 12px; }
  td { padding: 8px 12px; border: 1px solid #ddd; }
  tr:nth-child(even) { background: #f4f8ff; }
  code { background: #f0f0f0; padding: 2px 6px; border-radius: 4px; font-size: 22px; }
  section.dense { font-size: 22px; }
  section.dense table { font-size: 18px; }
  section.dense th, section.dense td { padding: 4px 10px; }
  section.dense code { font-size: 16px; padding: 1px 4px; }
  section.dense pre code { font-size: 17px; }
---

# DIGIT Web Automation Framework
### End-to-End Test Automation for DIGIT Platform

**Team:** QA / Engineering
**Platform:** DIGIT UAT, DEMO & Demo 2.0 Environments
**Date:** November 2026

---

## Agenda

1. Problem Statement
2. Solution — What We Built
3. Technology Stack
4. Framework Architecture
5. Test Coverage
6. CI/CD Integration
7. Reporting Dashboard
8. Live Demo
9. Benefits & Impact
10. Next Steps

---

## Problem Statement

Manual testing of the DIGIT platform was:

- **Time-consuming** — full regression took days per release
- **Error-prone** — human testers miss edge cases under time pressure
- **Not repeatable** — no consistent baseline between releases
- **No visibility** — no historical pass/fail trend for management
- **Blocking releases** — QA was the bottleneck before every deployment

> We needed automated, repeatable, CI-integrated tests that run without manual intervention.

---

## Solution — What We Built

A **Java + Playwright** end-to-end test automation framework covering the full DIGIT platform workflow.

| What | Details |
|------|---------|
| Language | Java 11 |
| Browser Automation | Microsoft Playwright |
| Test Framework | TestNG |
| Build Tool | Maven |
| Excel Handling | Apache POI |
| CI/CD | GitHub Actions + Jenkins |
| Reporting | Allure Dashboard |
| Environments | UAT, DEMO, Demo 2.0 |

---

## Technology Stack

```
┌─────────────────────────────────────────────┐
│              GitHub Actions CI/CD            │
├─────────────────────────────────────────────┤
│         Allure Report  →  GitHub Pages       │
├─────────────────────────────────────────────┤
│  TestNG 7.9 · Surefire · Parallel (config.)  │
├─────────────────────────────────────────────┤
│  Java 11 + Playwright 1.44 + Apache POI      │
├─────────────────────────────────────────────┤
│        Chromium  (headless in CI)            │
└─────────────────────────────────────────────┘
```

- **Playwright** — modern browser automation, faster and more reliable than Selenium
- **TestNG** — powerful test grouping, parallel execution, dependency management
- **Allure** — rich visual dashboard with trend history
- **Apache POI** — fills the campaign-specific Microplan Excel template before upload

---

## Framework Architecture

```
web-automation/
├── src/test/java/
│   ├── base/          BaseTest.java         (setup/teardown)
│   ├── pages/         Page Object Model     (12 pages + BasePage)
│   ├── tests/         Test Classes          (11 modules)
│   └── utils/         Helpers               (7 utility classes)
├── src/test/resources/
│   ├── testdata.properties                  (UAT / DEMO / QA data)
│   └── test files     (xlsx, pdf, jpeg)
├── .github/workflows/run-tests.yml          (GitHub Actions CI)
└── Jenkinsfile                              (Jenkins pipeline)
```

**Design Pattern: Page Object Model (POM)**
Each screen has a dedicated class — tests stay clean, locators are centralised, maintenance is easy.

---

## Page Object Model — 3-Layer Design

```
┌─────────────────────────────────────────────────────┐
│  TEST LAYER  (ComplaintTest.java)                   │
│  What to test — readable, no selectors, no waits   │
├─────────────────────────────────────────────────────┤
│  PAGE OBJECT LAYER  (ComplaintPage.java)            │
│  How to interact — all locators & actions here     │
├─────────────────────────────────────────────────────┤
│  BROWSER LAYER  (Playwright)                        │
│  Clicks, fills, waits, navigates                   │
└─────────────────────────────────────────────────────┘
```

**Rule:** Tests never contain CSS selectors or waits.
**Benefit:** If the UI changes, fix it in **one page class** — not in every test.

---

## POM Example — Complaints Module (Test Layer)

```java
// ComplaintTest.java — clean, readable, no UI details
@Test(groups = {"payments-ui", "Complaint", "sanity"})
public void resolveComplaint() {
    // Step 1: Create a complaint and capture its number
    ComplaintPage complaint = nav.goToCreateComplaint();
    String complaintNumber = complaint.fillForm("not working");

    // Step 2: Go to inbox and resolve it
    complaint.clickBackToComplaintsInbox();
    complaint.searchAndResolve(complaintNumber, "Issue fixed");

    // Step 3: Assert status updated
    Assert.assertTrue(complaint.isStatusVisible("Resolved"),
        "Status should be Resolved");
}
```

The test reads like plain English — **what** happens, not **how**.

---

<!-- _class: dense -->

## POM Example — Complaints Module (Page Layer)

```java
// ComplaintPage.java — all UI details hidden here
public void searchAndResolve(String complaintNumber, String comments) {
    searchComplaint(complaintNumber);   // fills input, clicks Search
    openComplaint(complaintNumber);     // clicks the complaint link
    resolve(comments, null);            // Take Action → Resolve → Submit
}

public void resolve(String comments, String filePath) {
    takeAction();       // waits for overlay, clicks "Take Action"
    clickResolve();     // clicks "Resolve" menu option
    enterComments(comments);
    clickSubmit();
}

// Locators defined once, used everywhere
private final Locator takeActionButton =
    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions()
        .setName("Take Action"));
```

Change a locator here → all tests using it are fixed automatically.

---

<!-- _class: dense -->

## Test Coverage — Modules

| Module | Test Class | Tests |
|--------|-----------|-------|
| Login | LoginTest | 3 |
| Home | HomeTest | 1 |
| Campaign Landing | CampaignLandingTest | 2 |
| Draft Campaign (creation) | DraftCampaignTest | 18 |
| Boundary Selection | BoundarySelectionTest | 8 |
| Configure Delivery Rules | ConfigureDeliveryRulesTest | 9 |
| App Configuration | AppConfigurationTest | 18 |
| Upload File | UploadFileTest | 10 |
| Create Checklist | CreateChecklistTest | 2 |
| Complaints (PGR) | ComplaintTest | 18 |
| HRMS | HRMSTest | 6 |
| **Total** | **11 modules** | **95 tests** |

---

<!-- _class: dense -->

## Test Coverage — Test Types

```
  95 Total Tests
  ├── 50  Positive / Sanity tests    (happy path flows)
  └── 45  Negative tests             (error handling, validation)
```

**What negative tests cover:**
- Submit without required fields
- Invalid file types / corrupted files
- Duplicate username / mobile number
- Campaign name validation (too long, special chars, emoji)
- Boundary selection with missing levels
- Date validation errors
- App Configuration: empty label / help text, min length above max

Both **BEDNET** and **MR-DN** campaign types tested independently for full coverage.

---

## Campaign Creation — Full E2E Flow

The framework automates the **complete campaign lifecycle**:

```
Login → Campaign Landing → Draft Campaign → Boundary Hierarchy
  → Boundary Selection → Configure Delivery Rules
    → App Configuration → Upload File → Create Checklist
```

Each step is independently testable AND chained together for full flow validation. The `NavigationHelper` provides a single entry point:

```java
// Navigate directly to any step in one line
DraftCampaignPage draft   = nav.goToBoundaryHierarchy();
BoundarySelectionPage page = nav.goToBoundarySelection();
UploadFilePage upload     = nav.goToUploadFile();
```

**Upload step:** `MicroplanTemplateFiller` edits the app-generated, campaign-specific Microplan workbook in place (targets, users, facilities) and uploads it back.

---

<!-- _class: dense -->

## CI/CD Integration — GitHub Actions

**Triggers:**
| Event | When |
|-------|------|
| Scheduled | Weekdays 11:00 AM IST — `DEMO` branch on DEMO env |
| Manual (`workflow_dispatch`) | Choose env (UAT / DEMO / Demo 2.0), groups, class, method, threads |
| Deployment (`repository_dispatch`) | Triggered by a deployment pipeline — runs on UAT |

**What the pipeline does:**
1. Checkout code → Setup Java 11
2. Install dependencies + Playwright Chromium
3. Create and validate `.env` from GitHub Secrets / Variables
4. Run tests (10 parallel classes by default)
5. Print test summary + upload failure screenshots
6. Generate Allure report (with history) → publish to GitHub Pages

---

## CI/CD — Parallel Execution

Test classes run **in parallel** — 10 by default, configurable per run — each test gets its own browser instance.

```xml
<parallel>classes</parallel>
<threadCount>${test.threads}</threadCount>   <!-- -Dtest.threads=10 -->
```

| Mode | Approximate Time |
|------|-----------------|
| Sequential (before) | ~60–90 min |
| Parallel (10 classes) | ~15–20 min |

**Thread-safe design:**
- Each test creates/destroys its own `Playwright → Browser → Page`
- `ThreadLocal` session cache ensures test data isolation between parallel tests
- No shared mutable state

---

<!-- _class: dense -->

## Test Grouping — Flexible Execution

Tests are tagged with groups. Run exactly what you need:

```bash
mvn test                              # full regression
mvn test -Dtest.groups="sanity"       # sanity only
mvn test -Dtest.groups="HRMS"         # specific module

# multiple groups, 5 parallel classes
mvn test -Dtest.groups="workbench-ui,Complaint" -Dtest.threads=5
```

| Group | Purpose |
|-------|---------|
| `smoke` / `sanity` | Quick health check |
| `regression` | Full Complaint + HRMS regression |
| `workbench-ui` | Campaign/workbench flows |
| `payments-ui` | Complaints (PGR) + HRMS flows |
| `HRMS` / `Complaint` | Single module |
| `negative` | Error/validation tests |
| `common` | Login (shared) |

---

<!-- _class: dense -->

## Allure Reporting Dashboard

After every CI run, a live dashboard is published to **GitHub Pages**.

**Dashboard shows:**
- ✅ Pass / ❌ Fail / ⏭ Skip counts
- Test duration per test and per suite
- **Trend chart** — pass/fail history across all runs
- Failure details with full stack trace
- Test categorisation by suite, class, method

**Access:**
```
https://lataganesh-egov.github.io/Web-Automation/
```

Also works locally — after any `mvn test` run:
```bash
allure serve target/allure-results
```

---

## Failure Screenshots

When a test fails, a screenshot is automatically captured and attached:

- Saved to `screenshots/FAILED_<ClassName>_<method>_<timestamp>.png`
- Uploaded as a GitHub Actions artifact (downloadable from CI run)
- Visible in the Allure report under the failed test

This means **zero manual investigation needed** to understand what the UI looked like at the point of failure.

---

## Environment Management

Tests run against **UAT**, **DEMO** or **Demo 2.0** — switched at runtime, no code changes.

**Secrets stored in GitHub** (never in code):
- `BASE_URL`, `USERNAME`, `PASSWORD`

**Non-sensitive config in GitHub Variables:**
- `BROWSER`, `HEADLESS`, `HIERARCHY_TYPE`, `COUNTRY`, `STATE`, `LGA`, `WARD`, `VILLAGE`, `AREA`

**Test data per environment** (`testdata.properties`):
```properties
uat.CAMPAIGN_TYPE=BEDNET,MR-DN
demo.CAMPAIGN_TYPE=BEDNET,MR-DN
HRMS_DEPARTMENT=Other,eGov      # random pick per test
```

`TestDataReader` detects the environment from `BASE_URL` (uat / demo / qa) and picks the right values automatically.

---

## Benefits & Impact

| Before Automation | After Automation |
|-------------------|-----------------|
| Manual regression: 2–3 days | Full regression: ~15–20 min |
| Missed edge cases | 45 negative tests catch regressions |
| No visibility on trends | Allure dashboard with history |
| QA bottleneck on releases | Daily scheduled runs + deployment-triggered runs |
| Environment setup by hand | One-click via GitHub Actions |
| No evidence for failures | Screenshots + reports attached |

**Key numbers:**
- **95 automated test cases** across 11 modules
- **10 parallel classes** (configurable) reducing CI run time
- **Zero manual steps** for scheduled daily runs

---

## Key Engineering Decisions

| Decision | Reason |
|----------|--------|
| Playwright over Selenium | Faster, auto-waits, better async handling |
| Page Object Model | Maintainable — locators in one place |
| TestNG over JUnit | Grouping, parallel, `@BeforeMethod` lifecycle |
| ThreadLocal session cache | Safe test data sharing across parallel tests |
| Allure over Surefire HTML | Rich dashboards, trend history, GitHub Pages |
| Apache POI for Microplan upload | App-generated template is campaign-specific — filled in place |
| Retry in navigation helpers | Absorbs slow DIGIT page loads in headless CI |
| GitHub Actions | Zero infra — runs on GitHub's cloud |

---

## What's Next — Roadmap

| Priority | Item |
|----------|------|
| High | Increase test coverage — remaining DIGIT modules |
| High | Add `@Step` annotations for step-level Allure traces |
| Medium | Email notification on failure (GitHub Actions) |
| Medium | Retry flaky tests automatically (Surefire `rerunFailingTestsCount`) |
| Medium | QA environment test data configuration |
| Medium | Wire Jenkins deployment jobs to trigger UI tests (QA / UAT / DEMO) |
| Low | Extend to mobile browser testing (Playwright supports it) |

---

## Summary

- **95 automated tests** covering the full DIGIT platform workflow
- **End-to-end** from Login → Campaign → HRMS → Complaints
- **Runs automatically** every weekday at 11:00 AM IST and after deployments
- **10 parallel classes** — full suite in under 20 minutes
- **Live Allure dashboard** with trend history on GitHub Pages
- **Zero infrastructure cost** — runs entirely on GitHub Actions

> The framework is production-ready, actively running in CI, and designed to scale as new features are added to the DIGIT platform.

---

# Thank You

**Questions?**

*Repository: github.com/LataNaik-eGov/Web-Automation*
*Dashboard: lataganesh-egov.github.io/Web-Automation*
