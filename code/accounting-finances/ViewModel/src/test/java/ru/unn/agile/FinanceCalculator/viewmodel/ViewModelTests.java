package ru.unn.agile.FinanceCalculator.viewmodel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.unn.agile.FinanceCalculator.Model.DayExpenses;
import ru.unn.agile.FinanceCalculator.Model.ExpensesType;
import ru.unn.agile.FinanceCalculator.Model.Money;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.GregorianCalendar;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.assertFalse;

public class ViewModelTests {
    private ViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new ViewModel();
    }

    @After
    public void tearDown() {
        viewModel = null;
    }

    @Test
    public void canSetDefaultValues() {
        assertEquals("", viewModel.eatingOutProperty().get());
        assertEquals("", viewModel.productsProperty().get());
        assertEquals("", viewModel.transportProperty().get());
        assertEquals("", viewModel.servicesProperty().get());
        assertEquals("", viewModel.entertainmentProperty().get());
        assertEquals("", viewModel.unreasonableWasteProperty().get());
        assertEquals(LocalDate.now(), viewModel.dateInputProperty().get());
        assertEquals(LocalDate.now(), viewModel.dateOutputProperty().get());
    }

    @Test
    public void canAddCostTwoTimesInOneDay() {
        setSubmitData();
        viewModel.submitCosts();
        setSubmitData();
        viewModel.submitCosts();
        viewModel.getCosts();
        assertEquals("20.00", viewModel.eatingOutProperty().get());
    }

    @Test
    public void canGetCostOfEmptyDay() {
        viewModel.dateOutputProperty().set(LocalDate.of(1999, 1, 2));
        viewModel.getCosts();
        assertEquals("0.00", viewModel.eatingOutProperty().get());
        assertEquals("0.00", viewModel.productsProperty().get());
        assertEquals("0.00", viewModel.transportProperty().get());
        assertEquals("0.00", viewModel.servicesProperty().get());
        assertEquals("0.00", viewModel.entertainmentProperty().get());
        assertEquals("0.00", viewModel.unreasonableWasteProperty().get());
    }

    @Test
    public void isResultCorrectWhenCostsAdded() {
        viewModel.inputExpensesCostProperty().set("10.37");
        viewModel.expensesTypesProperty().set(ExpensesType.Products);
        viewModel.dateInputProperty().set(LocalDate.of(2000, 1, 1));
        viewModel.submitCosts();
        Calendar date = GregorianCalendar.
                from(viewModel.getDateInput().atStartOfDay(ZoneId.systemDefault()));
        Money money = new Money("10.37");
        assertEquals(money, viewModel.expensesProperty().getCost(date, ExpensesType.Products));
    }

    @Test
    public void isResultCorrectWhenCostsGeting() {
        setDayExpensesData();
        viewModel.getCosts();
        assertEquals("77.88", viewModel.eatingOutProperty().get());
        assertEquals("9999.88", viewModel.productsProperty().get());
        assertEquals("1111.88", viewModel.transportProperty().get());
        assertEquals("100.88", viewModel.servicesProperty().get());
        assertEquals("777.88", viewModel.entertainmentProperty().get());
        assertEquals("99.70", viewModel.unreasonableWasteProperty().get());
    }

    @Test
    public void statusIsWaitingWhenSettingCostFieldsAreEmpty() {
        viewModel.submitCosts();
        assertEquals(StatusSubmit.WAITING.toString(), viewModel.submitStatusProperty().get());
    }

    @Test
    public void statusIsWaitingWhenGettingCostFieldsAreEmpty() {
        LocalDate date = null;
        viewModel.dateOutputProperty().set(date);
        assertEquals(StatusLoad.WAITING.toString(), viewModel.loadStatusProperty().get());
    }

    @Test
    public void statusIsWaitingWhenGettingCostFieldsAreFill() {
        viewModel.dateOutputProperty().set(LocalDate.of(2000, 1, 1));
        assertEquals(StatusLoad.READY.toString(), viewModel.loadStatusProperty().get());
    }

    @Test
    public void canReportTomorrowDateWhenGettingCost() {
        viewModel.dateOutputProperty().set(LocalDate.now().plusDays(9));
        assertEquals(StatusLoad.BAD_FORMAT_DATA.toString(), viewModel.loadStatusProperty().get());
    }

    @Test
    public void canReportTomorrowDateWhenSubmittingCost() {
        viewModel.dateInputProperty().set(LocalDate.now().plusDays(9));
        assertEquals(StatusSubmit.BAD_FORMAT_DATA.toString(),
                viewModel.submitStatusProperty().get());
    }

    @Test
    public void canChangeIncorrectStatusToWaitingStatus() {
        viewModel.dateInputProperty().set(LocalDate.now().plusDays(9));
        assertEquals(StatusSubmit.BAD_FORMAT_DATA.toString(),
                viewModel.submitStatusProperty().get());
        viewModel.dateInputProperty().set(LocalDate.now());
        assertEquals(StatusSubmit.WAITING.toString(),
                viewModel.submitStatusProperty().get());
    }

    @Test
    public void canChangeIncorrectStatusToReadyStatusSubmit() {
        viewModel.dateInputProperty().set(LocalDate.now().plusDays(9));
        assertEquals(StatusSubmit.BAD_FORMAT_DATA.toString(),
                viewModel.submitStatusProperty().get());
        setSubmitData();
        assertEquals(StatusSubmit.READY.toString(),
                viewModel.submitStatusProperty().get());
    }

    @Test
    public void canChangeIncorrectStatusToReadyStatusSetting() {
        viewModel.dateOutputProperty().set(LocalDate.now().plusDays(9));
        assertEquals(StatusLoad.BAD_FORMAT_DATA.toString(),
                viewModel.loadStatusProperty().get());
        viewModel.dateOutputProperty().set(LocalDate.now());
        assertEquals(StatusLoad.READY.toString(),
                viewModel.loadStatusProperty().get());
    }

    @Test
    public void statusIsReadyWhenSettingCostFieldsAreFill() {
        viewModel.inputExpensesCostProperty().set("10.37");
        viewModel.expensesTypesProperty().set(ExpensesType.Products);
        viewModel.dateInputProperty().set(LocalDate.of(2000, 1, 1));
        assertEquals(StatusSubmit.READY.toString(), viewModel.submitStatusProperty().get());
    }

    @Test
    public void buttonGetIsDisabledWhenDataIsBad() {
        viewModel.dateOutputProperty().set(LocalDate.now().plusDays(9));
        assertTrue(viewModel.isGetButtonDisabled());
    }

    @Test
    public void buttonGetIsEnabledWhenDataIsGood() {
        viewModel.dateOutputProperty().set(LocalDate.now());
        assertFalse(viewModel.isGetButtonDisabled());
    }

    @Test
    public void buttonAddIsDisabledWhenDataIsBad() {
        viewModel.dateInputProperty().set(LocalDate.now().plusDays(9));
        assertTrue(viewModel.isSetButtonDisabled());
    }

    @Test
    public void buttonAddIsEnabledWhenDataIsGood() {
        setSubmitData();
        assertFalse(viewModel.isSetButtonDisabled());
    }

    void setSubmitData() {
        viewModel.dateInputProperty().set(LocalDate.now());
        viewModel.inputExpensesCostProperty().set("10.0");
        viewModel.expensesTypesProperty().set(ExpensesType.EatingOut);
    }

    void setDayExpensesData() {
        DayExpenses input = new DayExpenses();
        Money unreasonableWasteMoney = new Money("99.70");
        input.add(ExpensesType.UnreasonableWaste, unreasonableWasteMoney);
        Money eatingOutMoney = new Money("77.88");
        input.add(ExpensesType.EatingOut, eatingOutMoney);
        Money productsMoney = new Money("9999.88");
        input.add(ExpensesType.Products, productsMoney);
        Money transportMoney = new Money("1111.88");
        input.add(ExpensesType.Transport, transportMoney);
        Money servicesMoney = new Money("100.88");
        input.add(ExpensesType.Services, servicesMoney);
        Money entertainmentMoney = new Money("777.88");
        input.add(ExpensesType.Entertainment, entertainmentMoney);
        viewModel.getDateInput().of(2009, Month.APRIL, 10);
        Calendar date = GregorianCalendar.
                from(viewModel.dateInputProperty().get().atStartOfDay(ZoneId.systemDefault()));
        viewModel.expensesProperty().addCost(input, date);
        viewModel.dateOutputProperty().set(viewModel.dateInputProperty().get());
    }

}
