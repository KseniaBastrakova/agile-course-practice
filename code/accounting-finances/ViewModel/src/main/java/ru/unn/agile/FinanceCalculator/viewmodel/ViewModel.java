package ru.unn.agile.FinanceCalculator.viewmodel;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import ru.unn.agile.FinanceCalculator.Model.Expenses;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.unn.agile.FinanceCalculator.Model.ExpensesType;
import ru.unn.agile.FinanceCalculator.Model.Money;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ViewModel {
    public ViewModel() {
        inputExpenses = new HashMap<>();
        for (ExpensesType type : ExpensesType.values()) {
            inputExpenses.put(type, new SimpleStringProperty(""));
        }

        inputExpensesCost.set("");
        submitStatus.set(StatusLoad.WAITING.toString());
        loadStatus.set(StatusLoad.READY.toString());
        expenses = new Expenses();
        dateInput.set(LocalDate.now());
        dateOutput.set(LocalDate.now());
        BooleanBinding couldAddValue = new BooleanBinding() {
            {
                super.bind(inputExpensesCost, dateInput, expensesTypes);
            }
            @Override
            protected boolean computeValue() {
                return getInputStatus() == StatusSubmit.READY;
            }
        };

        BooleanBinding couldGetValue = new BooleanBinding() {
            {
                super.bind(dateOutput);
            }
            @Override
            protected boolean computeValue() {
                return getOutputStatus() == StatusLoad.READY;
            }
        };

        setButtonDisabled.bind(couldAddValue.not());
        getButtonDisabled.bind(couldGetValue.not());

        final ValueChangeListenerDataSet listenerInputDate = new ValueChangeListenerDataSet();
        dateInput.addListener(listenerInputDate);
        valueChangedListenerDataSet.add(listenerInputDate);

        final ValueChangeListenerInputCostSet listenerInputCost
                = new ValueChangeListenerInputCostSet();
        inputExpensesCost.addListener(listenerInputCost);
        valueChangedListenerDouble.add(listenerInputCost);

        final ValueChangeListenerDateOutputSet listenerOutputDate
                = new ValueChangeListenerDateOutputSet();
        dateOutput.addListener(listenerOutputDate);
        valueChangedListenerDataOutputSet.add(listenerOutputDate);

       final ValueChangeListenerExpensesTypes listenerInputType
               = new ValueChangeListenerExpensesTypes();
        expensesTypes.addListener(listenerInputType);
        valueChangedListenerExpensesInputType.add(listenerInputType);
    }

    public BooleanProperty setButtonDisabledProperty() {
        return setButtonDisabled;
    }

    public final boolean isSetButtonDisabled() {
        return setButtonDisabled.get();
    }

    public BooleanProperty getButtonDisabledProperty() {
        return getButtonDisabled;
    }

    public final boolean isGetButtonDisabled() {
        return getButtonDisabled.get();
    }

    public StringProperty eatingOutProperty() {
        return inputExpenses.get(ExpensesType.EatingOut);
    }

    public final String getEatingOut() {
        return inputExpenses.get(ExpensesType.EatingOut).get();
    }

    public StringProperty productsProperty() {
        return inputExpenses.get(ExpensesType.Products);
    }

    public final String getProducts() {
        return inputExpenses.get(ExpensesType.Products).get();
    }

    public StringProperty unreasonableWasteProperty() {
        return inputExpenses.get(ExpensesType.UnreasonableWaste);
    }

    public final String getUnreasonableWaste() {
        return inputExpenses.get(ExpensesType.UnreasonableWaste).get();
    }

    public StringProperty transportProperty() {
        return inputExpenses.get(ExpensesType.Transport);
    }

    public final String getTransport() {
        return inputExpenses.get(ExpensesType.Transport).get();
    }

    public StringProperty servicesProperty() {
        return inputExpenses.get(ExpensesType.Services);
    }

    public final String getServices() {
        return inputExpenses.get(ExpensesType.Services).get();
    }

    public StringProperty entertainmentProperty() {
        return inputExpenses.get(ExpensesType.Entertainment);
    }

    public final String getEntertainment() {
        return inputExpenses.get(ExpensesType.Entertainment).get();
    }

    public Expenses expensesProperty() {
        return expenses;
    }

    public ObjectProperty<LocalDate> dateInputProperty() {
        return dateInput;
    }

    public LocalDate getDateInput() {
        return dateInput.get();
    }

    public LocalDate getDateOutput() {
        return dateOutput.get();
    }

    public ObjectProperty<LocalDate> dateOutputProperty() {
        return dateOutput;
    }

    public StringProperty inputExpensesCostProperty() {
        return inputExpensesCost;
    }

    public ObjectProperty<ObservableList<ExpensesType>> expensesTypeProperty() {
        return expensesType;
    }

    public final ObservableList<ExpensesType> getExpensesType() {
        return expensesType.get();
    }

    public StringProperty submitStatusProperty() {
        return submitStatus;
    }

    public final String getSubmitStatus() {
        return submitStatus.get();
    }

    public StringProperty loadStatusProperty() {
        return loadStatus;
    }

    public final String getLoadStatus() {
        return loadStatus.get();
    }

    public void submitCosts() {
        if (setButtonDisabled.get()) {
            return;
        }
        GregorianCalendar date =
                GregorianCalendar.from(dateInput.get().atStartOfDay(ZoneId.systemDefault()));
        ExpensesType type = expensesTypes.get();
        Money money = new Money(inputExpensesCost.get());
        expenses.addCost(money, date, type);
    }

    public void getCosts() {
       GregorianCalendar date =
               GregorianCalendar.from(dateOutput.get().atStartOfDay(ZoneId.systemDefault()));

        for (ExpensesType type : ExpensesType.values()) {
            inputExpenses.get(type).set(expenses.getCost(date, type).getAmount().toString());
        }
    }

    private StatusSubmit getInputStatus() {
        StatusSubmit inputStatusSubmit = StatusSubmit.READY;
        if ((dateInput == null || inputExpensesCostProperty().get().isEmpty())
                || expensesTypes.get() == null) {
            inputStatusSubmit = StatusSubmit.WAITING;
        }
        try {
            if (!inputExpensesCostProperty().get().isEmpty()) {
               double res = Double.parseDouble(inputExpensesCostProperty().get());
               if (res < 0) {
                   inputStatusSubmit = StatusSubmit.BAD_FORMAT;
               }
            }
        } catch (NumberFormatException nfe) {
            inputStatusSubmit = StatusSubmit.BAD_FORMAT;
        }
        if (dateInput != null && dateInput.get().isAfter(LocalDate.now())) {
                inputStatusSubmit = StatusSubmit.BAD_FORMAT_DATA;
            }
        return inputStatusSubmit;
    }

    private StatusLoad getOutputStatus() {
        StatusLoad outputStatusLoad = StatusLoad.READY;
        if (dateOutput.get() == null) {
            outputStatusLoad = StatusLoad.WAITING;
        }
        if (dateOutput.get() != null && dateOutput.get().isAfter(LocalDate.now())) {
                outputStatusLoad = StatusLoad.BAD_FORMAT_DATA;
        }
        return outputStatusLoad;
    }

    private class ValueChangeListenerDataSet implements ChangeListener<LocalDate> {
        @Override
        public void changed(final ObservableValue<? extends LocalDate> observable,
                            final LocalDate oldValue, final LocalDate newValue) {
            submitStatus.set(getInputStatus().toString());
        }
    }

    private class ValueChangeListenerInputCostSet implements ChangeListener<String> {
        @Override
        public void changed(final ObservableValue<? extends String> observable,
                            final String oldValue, final String newValue) {
            submitStatus.set(getInputStatus().toString());
        }
    }

    private class ValueChangeListenerDateOutputSet implements ChangeListener<LocalDate> {
        @Override
        public void changed(final ObservableValue<? extends LocalDate> observable,
                            final LocalDate oldValue, final LocalDate newValue) {
            loadStatus.set(getOutputStatus().toString());
        }
    }

    private class ValueChangeListenerExpensesTypes implements ChangeListener<ExpensesType> {
        @Override
        public void changed(final ObservableValue<? extends ExpensesType> observable,
                            final ExpensesType oldValue, final ExpensesType newValue) {
            submitStatus.set(getInputStatus().toString());
        }
    }

    public ObjectProperty<ExpensesType> expensesTypesProperty() {
        return expensesTypes;
    }

    private final Map<ExpensesType, StringProperty> inputExpenses;
    private final ObjectProperty<ObservableList<ExpensesType>> expensesType =
            new SimpleObjectProperty<>(FXCollections.observableArrayList(ExpensesType.values()));
    private final ObjectProperty<ExpensesType> expensesTypes = new SimpleObjectProperty<>();
    private final List<ValueChangeListenerDataSet> valueChangedListenerDataSet = new ArrayList<>();
    private final List<ValueChangeListenerDateOutputSet>
            valueChangedListenerDataOutputSet = new ArrayList<>();

    private final List<ValueChangeListenerExpensesTypes>
            valueChangedListenerExpensesInputType = new ArrayList<>();
    private final List<ValueChangeListenerInputCostSet>
            valueChangedListenerDouble = new ArrayList<>();
    private final ObjectProperty<LocalDate> dateInput = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> dateOutput = new SimpleObjectProperty<>();
    private final BooleanProperty setButtonDisabled = new SimpleBooleanProperty();
    private final StringProperty submitStatus = new SimpleStringProperty();
    private final BooleanProperty getButtonDisabled = new SimpleBooleanProperty();
    private final StringProperty loadStatus = new SimpleStringProperty();
    private final StringProperty inputExpensesCost = new SimpleStringProperty();
    private Expenses expenses;
}

enum StatusSubmit {
    WAITING("Please, input data"),
    READY("Click 'add expenses'"),
    BAD_FORMAT_DATA("Date can't be after today"),
    BAD_FORMAT("Bad format for input expenses");

    StatusSubmit(final String name) {
        this.name = name;
    }
    public String toString() {
        return name;
    }
    private final String name;
}

enum StatusLoad {
    WAITING("Please, input data"),
    READY("Click 'get expenses'"),
    BAD_FORMAT_DATA("Date can't be after today");

    StatusLoad(final String name) {
        this.name = name;
    }
    public String toString() {
        return name;
    }
    private final String name;
}

