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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class ViewModel {
    private final StringProperty eatingOut = new SimpleStringProperty();
    private final StringProperty products = new SimpleStringProperty();
    private final StringProperty unreasonableWaste = new SimpleStringProperty();
    private final StringProperty transport = new SimpleStringProperty();
    private final StringProperty services = new SimpleStringProperty();
    private final StringProperty entertainment = new SimpleStringProperty();
    private final StringProperty inputRubles = new SimpleStringProperty();
    private final StringProperty inputKopek = new SimpleStringProperty();
    private final ObjectProperty<ObservableList<Expenses.Operation>> operations =
            new SimpleObjectProperty<>(FXCollections.observableArrayList(Expenses.Operation.values()));
    private final ObjectProperty<Expenses.Operation> operation = new SimpleObjectProperty<>();
    private final List<ValueChangeListener> valueChangedListeners = new ArrayList<>();

    private final ObjectProperty<LocalDate> dateInput = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> dateOutput = new SimpleObjectProperty<>();
    public ObjectProperty<Expenses.Operation> operationProperty() {
        return operation;
    }


    private final BooleanProperty getDisabled = new SimpleBooleanProperty();
    private final BooleanProperty setDisabled = new SimpleBooleanProperty();
    private final StringProperty statusGetting = new SimpleStringProperty();
    private final StringProperty statusSetting = new SimpleStringProperty();
    private final StringProperty inputExpences = new SimpleStringProperty();
    private Expenses expenses;



    // FXML needs default c-tor for binding
    public ViewModel() {
        eatingOut.set("");
        products.set("");
        unreasonableWaste.set("");
        transport.set("");
        services.set("");
        entertainment.set("");
        inputRubles.set("");
        inputKopek.set("");
        statusGetting.set(StatusGettingCost.WAITING.toString());
        statusSetting.set(StatusInputCost.WAITING.toString());
        expenses = new Expenses();
        dateInput.set(LocalDate.now());
        dateOutput.set(LocalDate.now());
        BooleanBinding couldAddValue = new BooleanBinding() {
            {
                super.bind(inputRubles, inputKopek, dateInput);
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
        getDisabled.bind(couldAddValue.not());
        setDisabled.bind(couldGetValue.not());


        final ValueChangeListenerDateGet listener = new ValueChangeListenerDateGet();
        dateOutput.addListener(listener);
        valueChangedListeners.add(listener);

        final ValueChangeListenerDateSet listnerDate = new ValueChangeListenerDateSet();


    }

    public StringProperty eatingOutProperty() {
        return eatingOut;
    }
    public StringProperty productsProperty() {
        return products;
}
    public StringProperty unreasonableWasteProperty() {
        return unreasonableWaste;
    }
    public StringProperty transportProperty() {
        return transport;
    }
    public StringProperty servicesProperty() {
        return services;
    }
    public StringProperty entertainmentProperty() {
        return entertainment;
    }
    public StringProperty inputExpencesProperty(){ return inputExpences; }
    public Expenses expensesProperty() { return expenses; }

    public StringProperty inputRublesProperty() {
        return inputRubles;
    }

    public StringProperty inputKopekProperty() {
        return inputKopek;
    }
    public ObjectProperty<LocalDate> dateInputProperty() {
        return dateInput;
    }

    public ObjectProperty<LocalDate> dateOutputProperty() {
        return dateOutput;
    }
    //setDisabled

    public BooleanProperty setDisabledProperty() {
        return setDisabled;
    }
    public final boolean getSetDisabled() {
        return setDisabled.get();
    }


    public BooleanProperty getDisabledProperty() {
        return getDisabled;
    }
    public final boolean getGetDisabled() {
        return getDisabled.get();
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

    public void set() {
        if (!setDisabled.get()) {
            return;
        }
        GregorianCalendar date = GregorianCalendar.from(dateInput.get().atStartOfDay(ZoneId.systemDefault()));

        Money money = new Money(inputExpences.get());
        operation.get().apply(expenses, money, date);
    }

    public void getCosts() {

        if (getDisabled.get()) {
            return;
        }
       GregorianCalendar date = GregorianCalendar.from(dateOutput.get().atStartOfDay(ZoneId.systemDefault()));
       eatingOut.set(expenses.getCost(date, ExpensesType.EatingOut).getAmount().toString());
       products.set(expenses.getCost(date, ExpensesType.Products).getAmount().toString());
       unreasonableWaste.set(expenses.getCost(date, ExpensesType.UnreasonableWaste).getAmount().toString());
       transport.set(expenses.getCost(date, ExpensesType.Transport).getAmount().toString());
       services.set(expenses.getCost(date, ExpensesType.Services).getAmount().toString());
       entertainment.set(expenses.getCost(date, ExpensesType.Entertainment).getAmount().toString());
    }

    private StatusInputCost getInputCostStatus() {
        StatusInputCost inputStatus = StatusInputCost.READY;
        if ((dateInput == null && inputRubles.get().isEmpty() && inputKopek.get().isEmpty())) {
            inputStatus = StatusInputCost.WAITING;
         }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
            if (!(dateInput == null)) {
                //
            }
            if (!inputRubles.get().isEmpty()) {
                Double.parseDouble(inputRubles.get());
            }
            if (!inputKopek.get().isEmpty()) {
                Double.parseDouble(inputKopek.get());
            }
        } catch (NumberFormatException nfe) {
            inputStatus = StatusInputCost.BAD_FORMAT;
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

    private class ValueChangeListenerDateSet implements ChangeListener<String> {
        @Override
        public void changed(final ObservableValue<? extends String> observable,
                            final String oldValue, final String newValue) {
            statusSetting.set(getInputCostStatus().toString());
        }
    }

    private class ValueChangeListenerDoubleSet implements ChangeListener<LocalDate> {
        @Override
        public void changed(final ObservableValue<? extends LocalDate> observable,
                            final LocalDate oldValue, final LocalDate newValue) {
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
