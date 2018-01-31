package ru.unn.agile.RatioCalculator.viewmodel;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.unn.agile.RatioCalculator.Model.Ratio.Operation;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

public class ViewModelTests {

    public void setViewModel(final ViewModel theViewModel) {
        this.viewModel = theViewModel;
    }

    @Before
    public void setUp() {
        if (viewModel == null) {
            viewModel = new ViewModel(new FakeLogger());
        }
    }

    @After
    public void tearDown() {
        viewModel = null;
    }

    @Test
    public void statusIsReadyWhenFieldsAreFill() {
        setInputData("1", "2", "3", "4");
        assertEquals(Status.READY.toString(), viewModel.statusProperty().get());
    }

    @Test
    public void canSetDefaultValues() {
        assertEquals("", viewModel.denominatorFirstProperty().get());
        assertEquals("", viewModel.numeratorFirstProperty().get());
        assertEquals("", viewModel.denominatorSecondProperty().get());
        assertEquals("", viewModel.numeratorSecondProperty().get());
        assertEquals(Operation.ADD, viewModel.operationProperty().get());
        assertEquals(Status.WAITING.toString(), viewModel.statusProperty().get());
    }

    @Test
    public void canNotDivOnZero() {
        viewModel.operationProperty().set(Operation.DIV);
        setInputData("1", "2", "3", "0");
        assertEquals(Status.BAD_FORMAT.toString(), viewModel.statusProperty().get());
    }

    @Test
    public void canReportBadFormat() {
        viewModel.denominatorFirstProperty().set("a");

        assertEquals(Status.BAD_FORMAT.toString(), viewModel.statusProperty().get());
    }

    @Test
    public void statusIsWaitingIfNotEnoughCorrectData() {
        viewModel.denominatorFirstProperty().set("1");

        assertEquals(Status.WAITING.toString(), viewModel.statusProperty().get());
    }

    @Test
    public void canSetAddOperation() {
        viewModel.operationProperty().set(Operation.ADD);
        assertEquals(Operation.ADD, viewModel.operationProperty().get());
    }

    @Test
    public void addIsDefaultOperation() {
        assertEquals(Operation.ADD, viewModel.operationProperty().get());
    }

    @Test
    public void statusIsWaitingWhenCalculateWithEmptyFields() {
        viewModel.calculate();
        assertEquals(Status.WAITING.toString(), viewModel.statusProperty().get());
    }

    @Test
    public void statusIsBadFormatIfDenominatorIsZero() {
        setInputData("0", "1", "9", "2");
        assertEquals(Status.BAD_FORMAT.toString(), viewModel.statusProperty().get());
    }

    @Test
    public void statusIsReadyIfDenominatorChangedFromZeroToCorrectValue() {
        setInputData("0", "2", "3", "4");
        assertEquals(Status.BAD_FORMAT.toString(), viewModel.statusProperty().get());
        viewModel.denominatorFirstProperty().set("999");
        assertEquals(Status.READY.toString(), viewModel.statusProperty().get());
    }

    @Test
    public void operationAddHasCorrectResult() {
        setInputData("9", "1", "9", "2");
        viewModel.calculate();
        assertEquals("1", viewModel.resultNumeratorProperty().get());
        assertEquals("3", viewModel.resultDenominatorProperty().get());
    }

    @Test
    public void operationMulHasCorrectResult() {
        setInputData("2", "1", "2", "1");
        viewModel.operationProperty().set(Operation.MULTIPLY);
        viewModel.calculate();
        assertEquals("1", viewModel.resultNumeratorProperty().get());
        assertEquals("4", viewModel.resultDenominatorProperty().get());
    }

    @Test
    public void operationSubHasCorrectResult() {
        setInputData("6", "1", "12", "1");
        viewModel.operationProperty().set(Operation.SUB);
        viewModel.calculate();
        assertEquals("1", viewModel.resultNumeratorProperty().get());
        assertEquals("12", viewModel.resultDenominatorProperty().get());
    }

    @Test
    public void operationDivHasCorrectResult() {
        setInputData("6", "1", "6", "1");
        viewModel.operationProperty().set(Operation.DIV);
        viewModel.calculate();
        assertEquals("1", viewModel.resultNumeratorProperty().get());
        assertEquals("1", viewModel.resultDenominatorProperty().get());
    }

    @Test(expected = IllegalArgumentException.class)
    public void canNotCreateViewModelWithNullLogger() {
        new ViewModel(null);
    }

    @Test
    public void logIsEmptyInTheBeginning() {
        List<String> log = viewModel.getLog();

        assertTrue(log.isEmpty());
    }

    @Test
    public void logContainsCorrectMessageAfterCalculation() {
        setInputData("3", "4", "5", "6");
        viewModel.calculate();
        String message = viewModel.getLog().get(0);

        assertTrue(message.matches(".*" + LogMessages.CALCULATE_WAS_PRESSED + ".*"));
    }

    @Test
    public void logContainsCorrectArgumentsAfterCalculation() {
        setInputData("1", "2", "3",  "4");
        viewModel.calculate();
        String message = viewModel.getLog().get(0);
        checkOutputData(message);
    }

    @Test
    public void logContainsCorrectMessageAfterOperationChange() {
        setInputData("7", "8", "9", "9");
        viewModel.onOperationChanged(Operation.ADD, Operation.MULTIPLY);
        String text = viewModel.getLog().get(0);
        assertTrue(text.matches(".*" + LogMessages.OPERATION_WAS_CHANGED + "Mul.*"));
    }


    @Test
    public void logContainsSixMessagesWhenUserInputsAllDataAndPushCalculate() {
        viewModel.denominatorFirstProperty().set("1");
        viewModel.onFocusChanged(Boolean.TRUE, Boolean.FALSE);
        viewModel.numeratorFirstProperty().set("2");
        viewModel.onFocusChanged(Boolean.TRUE, Boolean.FALSE);
        viewModel.denominatorSecondProperty().set("3");
        viewModel.onFocusChanged(Boolean.TRUE, Boolean.FALSE);
        viewModel.numeratorSecondProperty().set("4");
        viewModel.onFocusChanged(Boolean.TRUE, Boolean.FALSE);
        viewModel.onOperationChanged(Operation.ADD, Operation.MULTIPLY);
        viewModel.onFocusChanged(Boolean.TRUE, Boolean.FALSE);
        viewModel.calculate();
        Assert.assertEquals(6, viewModel.getLog().size());
    }

    @Test
    public void logContainsCorrectOperationMessageAfterCalculation() {
        setInputData("3", "3", "4", "5");
        viewModel.calculate();
        String text = viewModel.getLog().get(0);
        assertTrue(text.matches(".*Add.*"));
    }

    @Test
    public void logContainsCorrectEditFieldsMessage() {
        setInputData("1", "1", "1", "1");
        viewModel.onFocusChanged(Boolean.TRUE, Boolean.FALSE);
        String message = viewModel.getLog().get(0);
        assertTrue(message.matches(".*" + LogMessages.EDITING_FINISHED
                + "Input arguments:"
                + viewModel.numeratorFirstProperty().get() + "/"
                + viewModel.denominatorFirstProperty().get() + "; "
                + viewModel.numeratorSecondProperty().get() + "/"
                + viewModel.denominatorSecondProperty().get()));
    }

    @Test
    public void logContainsArgumentsAfterCalculation() {
        setInputData("1", "1", "1", "1");
        viewModel.calculate();
        String message = viewModel.getLog().get(0);
        assertTrue(message.matches(".* Arguments"
                + ":first ratio = " + viewModel.numeratorFirstProperty().get()
                + "/" + viewModel.denominatorFirstProperty().get()
                + ";second ratio = " + viewModel.numeratorSecondProperty().get()
                + "/" + viewModel.denominatorSecondProperty().get() + " .*"));
    }


    @Test
    public void logDoesNotContainMessageAfterFocusChangeWithNoEdit() {
        viewModel.denominatorSecondProperty().set("12");
        viewModel.onFocusChanged(Boolean.TRUE, Boolean.FALSE);
        viewModel.denominatorSecondProperty().set("12");
        viewModel.onFocusChanged(Boolean.TRUE, Boolean.FALSE);
        Assert.assertEquals(1, viewModel.getLog().size());
    }

    @Test
    public void operationIsNotLoggedIfOperationNotChanged() {
        viewModel.onOperationChanged(Operation.ADD, Operation.MULTIPLY);
        viewModel.onOperationChanged(Operation.MULTIPLY, Operation.MULTIPLY);
        Assert.assertEquals(1, viewModel.getLog().size());
    }

    void checkOutputData(final String message) {
        assertTrue(message.matches(".*" + viewModel.numeratorFirstProperty().get()
                + ".*" + viewModel.denominatorFirstProperty().get()
                + ".*" + viewModel.numeratorSecondProperty().get()
                + ".*" + viewModel.denominatorSecondProperty().get() + ".*"));
    }

    private void setInputData(final String denominatorFirst, final String numeratorFirst,
                              final String denominatorSecond, final String numeratorSecond) {
        viewModel.denominatorFirstProperty().set(denominatorFirst);
        viewModel.numeratorFirstProperty().set(numeratorFirst);
        viewModel.denominatorSecondProperty().set(denominatorSecond);
        viewModel.numeratorSecondProperty().set(numeratorSecond);
    }
    private ViewModel viewModel;
}
