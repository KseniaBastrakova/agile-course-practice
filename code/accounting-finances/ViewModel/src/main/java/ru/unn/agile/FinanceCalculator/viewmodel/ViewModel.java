package ru.unn.agile.FinanceCalculator.viewmodel;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import ru.unn.agile.FinanceCalculator.Model.*;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class ViewModel {
    public BooleanProperty setButtonDisabledProperty() {
        return setButtonDisabled;
    }

    public final boolean isSetButtonDisabled() {
        return setButtonDisabled.get();
    }


    public ViewModel() {
        inputExpenses = new HashMap<>();
        for (ExpensesType type : ExpensesType.values()) {
            inputExpenses.put(type, new SimpleStringProperty(""));
        }

        inputExpensesCost.set("");
        statusGetting.set(Status.WAITING.toString());
        statusSetting.set(Status.WAITING.toString());
        expenses = new Expenses();
        dateInput.set(LocalDate.now());
        dateOutput.set(LocalDate.now());
        BooleanBinding couldAddValue = new BooleanBinding() {
            {
                super.bind(inputExpensesCost, dateInput);
            }
            @Override
            protected boolean computeValue() {
                return getInputStatus() == Status.READY;
            }
        };

        setButtonDisabled.bind(couldAddValue.not());


        final ValueChangeListenerDateSet listenerInputDate = new ValueChangeListenerDateSet();
        dateInput.addListener(listenerInputDate);
        valueChangedListenerDataSet.add(listenerInputDate);

        final ValueChangeListenerDateGet listnerOutputDate = new ValueChangeListenerDateGet();
        dateOutput.addListener(listnerOutputDate);
        valueChangedListenerDataGet.add(listnerOutputDate);

        final ValueChangeListenerDoubleSet listenerInputCost = new ValueChangeListenerDoubleSet();
        costInput.addListener(listenerInputCost);
        valueChangedListenerDouble.add(listenerInputCost);
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

    public StringProperty statusSettingProperty() {
        return statusSetting;
    }

    public final String getStatusSetting() {
        return statusSetting.get();
    }

    public StringProperty statusGettingProperty() {
        return statusGetting;
    }
    public final String getStatusGetting() {
        return statusGetting.get();
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

    private Status getInputStatus() {
        Status inputStatus = Status.READY;
        if ((dateInput == null || inputExpensesCostProperty().get().isEmpty())) {
            inputStatus = Status.WAITING;
        }

        return inputStatus;
    }

    private Status getGettingStatus() {
        Status inputStatus = Status.READY;
        if (dateOutput == null) {
            inputStatus = Status.WAITING;
        }
        return inputStatus;
    }

    private class ValueChangeListenerDateGet implements ChangeListener<LocalDate> {
        @Override
        public void changed(final ObservableValue<? extends LocalDate> observable,
                            final LocalDate oldValue, final LocalDate newValue) {
            statusGetting.set(getGettingStatus().toString());
        }
    }

    private class ValueChangeListenerDateSet implements ChangeListener<LocalDate> {
        @Override
        public void changed(final ObservableValue<? extends LocalDate> observable,
                            final LocalDate oldValue, final LocalDate newValue) {
            statusSetting.set(getInputStatus().toString());
        }
    }

    private class ValueChangeListenerDoubleSet implements ChangeListener<Double> {
        @Override
        public void changed(final ObservableValue<? extends Double> observable,
                            final Double oldValue, final Double newValue) {
            statusSetting.set(getInputStatus().toString());
        }
    }

    private final Map<ExpensesType, StringProperty> inputExpenses;
    private final ObjectProperty<ObservableList<ExpensesType>> expensesType =
            new SimpleObjectProperty<>(FXCollections.observableArrayList(ExpensesType.values()));
    private final ObjectProperty<ExpensesType> expensesTypes = new SimpleObjectProperty<>();
    private final List<ValueChangeListenerDateSet> valueChangedListenerDataSet = new ArrayList<>();
    private final List<ValueChangeListenerDateGet> valueChangedListenerDataGet = new ArrayList<>();
    private final List<ValueChangeListenerDoubleSet> valueChangedListenerDouble = new ArrayList<>();
    private final ObjectProperty<LocalDate> dateInput = new SimpleObjectProperty<>();
    private final ObjectProperty<Double> costInput = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> dateOutput = new SimpleObjectProperty<>();

    public ObjectProperty<ExpensesType> expensesTypesProperty() {
        return expensesTypes;
    }

    private final BooleanProperty setButtonDisabled = new SimpleBooleanProperty();
    private final StringProperty statusGetting = new SimpleStringProperty();
    private final StringProperty statusSetting = new SimpleStringProperty();
    private final StringProperty inputExpensesCost = new SimpleStringProperty();
    private Expenses expenses;
}

enum Status {
    WAITING("Please, input data"),
    READY("Click 'add Expenses'");

    Status(final String name) {
        this.name = name;
    }
    public String toString() {
        return name;
    }
    private final String name;
}

