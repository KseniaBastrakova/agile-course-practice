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
    public void isAddCostsCorrectResult() {
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
    public void isGetCostsCorrectResult() {
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
    public void statusIsWaitingWhenFieldsSettingCostAreEmpty() {
        viewModel.submitCosts();
        assertEquals(Status.WAITING.toString(), viewModel.statusSettingProperty().get());
    }

    @Test
    public void statusIsReadyWhenWhenFieldsSettingCostAreFill() {
        viewModel.inputExpensesCostProperty().set("10.37");
        viewModel.expensesTypesProperty().set(ExpensesType.Products);
        viewModel.dateInputProperty().set(LocalDate.of(2000, 1, 1));
        assertEquals(Status.READY.toString(), viewModel.statusSettingProperty().get());
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
