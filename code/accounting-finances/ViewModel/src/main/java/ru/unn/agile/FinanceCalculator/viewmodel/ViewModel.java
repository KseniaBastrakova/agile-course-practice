package ru.unn.agile.FinanceCalculator.viewmodel;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import ru.unn.agile.FinanceCalculator.Model.Expenses;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.unn.agile.FinanceCalculator.Model.ExpensesType;
import ru.unn.agile.FinanceCalculator.Model.Money;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class ViewModel {
    public ViewModel() {
        inputExpenses = new HashMap<>();
        for (ExpensesType type : ExpensesType.values()) {
            inputExpenses.put(type, new SimpleStringProperty(""));
        }

        inputExpensesCost.set("");
        statusSetting.set(Status.WAITING.toString());
        statusGetting.set(Status.WAITING.toString());
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

        BooleanBinding couldGetValue = new BooleanBinding() {
            {
                super.bind(dateOutput);
            }
            @Override
            protected boolean computeValue() {
                return getOutputStatus() == Status.READY;
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
        if ((dateInput == null || inputExpensesCostProperty().get().isEmpty())
                || expensesTypes.get() == null) {
            inputStatus = Status.WAITING;
        }
        try {
            if (!inputExpensesCostProperty().get().isEmpty()) {
               double res = Double.parseDouble(inputExpensesCostProperty().get());
               if (res < 0) {
                   inputStatus = Status.BAD_FORMAT;
               }
            }
        } catch (NumberFormatException nfe) {
            inputStatus = Status.BAD_FORMAT;
        }
        if (dateInput != null && dateInput.get().isAfter(LocalDate.now())) {
                inputStatus = Status.BAD_FORMAT_DATA;
            }
        return inputStatus;
    }

    private Status getOutputStatus() {
        Status outputStatus = Status.READY;
        if (dateOutput == null) {
            outputStatus = Status.WAITING;
        }
        if (dateOutput != null && dateOutput.get().isAfter(LocalDate.now())) {
                outputStatus = Status.BAD_FORMAT_DATA;
        }
        return outputStatus;
    }



    private class ValueChangeListenerDataSet implements ChangeListener<LocalDate> {
        @Override
        public void changed(final ObservableValue<? extends LocalDate> observable,
                            final LocalDate oldValue, final LocalDate newValue) {
            statusSetting.set(getInputStatus().toString());
        }
    }

    private class ValueChangeListenerInputCostSet implements ChangeListener<String> {
        @Override
        public void changed(final ObservableValue<? extends String> observable,
                            final String oldValue, final String newValue) {
            statusSetting.set(getInputStatus().toString());
        }
    }

    private class ValueChangeListenerDateOutputSet implements ChangeListener<LocalDate> {
        @Override
        public void changed(final ObservableValue<? extends LocalDate> observable,
                            final LocalDate oldValue, final LocalDate newValue) {
            statusGetting.set(getOutputStatus().toString());
        }
    }

    private final Map<ExpensesType, StringProperty> inputExpenses;
    private final ObjectProperty<ObservableList<ExpensesType>> expensesType =
            new SimpleObjectProperty<>(FXCollections.observableArrayList(ExpensesType.values()));
    private final ObjectProperty<ExpensesType> expensesTypes = new SimpleObjectProperty<>();
    private final List<ValueChangeListenerDataSet> valueChangedListenerDataSet = new ArrayList<>();
    private final List<ValueChangeListenerDateOutputSet>
            valueChangedListenerDataOutputSet = new ArrayList<>();
    private final List<ValueChangeListenerInputCostSet>
            valueChangedListenerDouble = new ArrayList<>();
    private final ObjectProperty<LocalDate> dateInput = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> dateOutput = new SimpleObjectProperty<>();

    public ObjectProperty<ExpensesType> expensesTypesProperty() {
        return expensesTypes;
    }

    private final BooleanProperty setButtonDisabled = new SimpleBooleanProperty();
    private final StringProperty statusSetting = new SimpleStringProperty();
    private final BooleanProperty getButtonDisabled = new SimpleBooleanProperty();
    private final StringProperty statusGetting = new SimpleStringProperty();
    private final StringProperty inputExpensesCost = new SimpleStringProperty();
    private Expenses expenses;
}

enum Status {
    WAITING("Please, input data"),
    READY("Click button"),
    BAD_FORMAT_DATA("Date can't be after today"),
    BAD_FORMAT("Bad format"),;

    Status(final String name) {
        this.name = name;
    }
    public String toString() {
        return name;
    }
    private final String name;
}

