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
    private final Map<ExpensesType, StringProperty> inputExpenses;
    private final ObjectProperty<ObservableList<ExpensesType>> operations =
            new SimpleObjectProperty<>(FXCollections.observableArrayList(ExpensesType.values()));
    private final ObjectProperty<ExpensesType> operation = new SimpleObjectProperty<>();
    private final List<ValueChangeListenerDateSet> valueChangedListenerDataSet = new ArrayList<>();
    private final List<ValueChangeListenerDateGet> valueChangedListenerDataGet = new ArrayList<>();
    private final List<ValueChangeListenerDoubleSet> valueChangedListenerDouble = new ArrayList<>();
    private final ObjectProperty<LocalDate> dateInput = new SimpleObjectProperty<>();
    private final ObjectProperty<Double> costInput = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> dateOutput = new SimpleObjectProperty<>();

    public ObjectProperty<ExpensesType> operationProperty() {
        return operation;
    }



    private final BooleanProperty setDisabled = new SimpleBooleanProperty();
    private final BooleanProperty getDisabled = new SimpleBooleanProperty();
    private final StringProperty statusGetting = new SimpleStringProperty();
    private final StringProperty statusSetting = new SimpleStringProperty();
    private final StringProperty inputExpencesCost = new SimpleStringProperty();
    private Expenses expenses;

    public BooleanProperty setDisabledProperty() {
        return setDisabled;
    }

    public final boolean isSetDisabled() {
        return setDisabled.get();
    }

    public BooleanProperty getDisabledProperty() {
        return getDisabled;
    }

    public final boolean isGetDisabled() {
        return getDisabled.get();
    }


    // FXML needs default c-tor for binding
    public ViewModel() {
        inputExpenses = new HashMap<>();
        for (ExpensesType type : ExpensesType.values()) {
            inputExpenses.put(type, new SimpleStringProperty(""));
        }

        inputExpencesCost.set("");
        statusGetting.set(StatusGettingCost.WAITING.toString());
        statusSetting.set(StatusInputCost.WAITING.toString());
        expenses = new Expenses();
        dateInput.set(LocalDate.now());
        dateOutput.set(LocalDate.now());
        BooleanBinding couldAddValue = new BooleanBinding() {
            {
                super.bind(inputExpencesCost, dateInput);
            }
            @Override
            protected boolean computeValue() {
                return getInputCostStatus() == StatusInputCost.READY;
            }
        };

        BooleanBinding couldGetValue = new BooleanBinding() {
            {
                super.bind(dateOutput);
            }
            @Override
            protected boolean computeValue() {
                return getGettingCostStatus() == StatusGettingCost.READY;
            }
        };
        getDisabled.bind(couldGetValue.not());
        setDisabled.bind(couldAddValue.not());


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
  //  public StringProperty inputExpencesProperty(){ return inputExpences; }
    public Expenses expensesProperty() {
        return expenses;
    }

    public ObjectProperty<LocalDate> dateInputProperty() {
        return dateInput;
    }

    public ObjectProperty<LocalDate> dateOutputProperty() {
        return dateOutput;
    }

    public StringProperty inputExpencesCostProperty() {
        return inputExpencesCost;
    }

    public ObjectProperty<ObservableList<ExpensesType>> operationsProperty() {
        return operations;
    }
    public final ObservableList<ExpensesType> getOperations() {
        return operations.get();
    }
    //setDisabled

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

    public void set() {
        if (setDisabled.get()) {
            return;
        }
        GregorianCalendar date =
                GregorianCalendar.from(dateInput.get().atStartOfDay(ZoneId.systemDefault()));
        ExpensesType type = operation.get();
        Money money = new Money(inputExpencesCost.get());
        expenses.addCost(money, date, type);
    }

    public void getCosts() {

        if (getDisabled.get()) {
            return;
        }
       GregorianCalendar date =
               GregorianCalendar.from(dateOutput.get().atStartOfDay(ZoneId.systemDefault()));

        for (ExpensesType type : ExpensesType.values()) {
            inputExpenses.get(type).set(expenses.getCost(date, type).getAmount().toString());
        }
    }

    private StatusInputCost getInputCostStatus() {
        StatusInputCost inputStatus = StatusInputCost.READY;
        if ((dateInput == null || inputExpencesCostProperty().get().isEmpty())) {
            inputStatus = StatusInputCost.WAITING;
        }

        return inputStatus;
    }

    private StatusGettingCost getGettingCostStatus() {
        StatusGettingCost inputStatus = StatusGettingCost.READY;
        if (dateOutput == null) {
            inputStatus = StatusGettingCost.WAITING;
        }
        return inputStatus;
    }

    private class ValueChangeListenerDateGet implements ChangeListener<LocalDate> {
        @Override
        public void changed(final ObservableValue<? extends LocalDate> observable,
                            final LocalDate oldValue, final LocalDate newValue) {
            statusGetting.set(getGettingCostStatus().toString());
        }
    }

    private class ValueChangeListenerDateSet implements ChangeListener<LocalDate> {
        @Override
        public void changed(final ObservableValue<? extends LocalDate> observable,
                            final LocalDate oldValue, final LocalDate newValue) {
            statusSetting.set(getInputCostStatus().toString());
        }
    }

    private class ValueChangeListenerDoubleSet implements ChangeListener<Double> {
        @Override
        public void changed(final ObservableValue<? extends Double> observable,
                            final Double oldValue, final Double newValue) {
            statusSetting.set(getInputCostStatus().toString());
        }
    }
}

enum StatusInputCost {
    WAITING("Please provide input data"),
    READY("Press setCost"),
    BAD_FORMAT("BadFormat");

    private final String name;
    StatusInputCost(final String name) {
        this.name = name;
    }
    public String toString() {
        return name;
    }
}

enum StatusGettingCost {
    WAITING("Please input data"),
    READY("Press getCost");

    private final String name;
    StatusGettingCost(final String name) {
        this.name = name;
    }
    public String toString() {
        return name;
    }
}
