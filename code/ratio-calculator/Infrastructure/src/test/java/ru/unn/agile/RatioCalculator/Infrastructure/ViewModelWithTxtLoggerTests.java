package ru.unn.agile.RatioCalculator.Infrastructure;

import ru.unn.agile.RatioCalculator.viewmodel.ViewModelTests;
import ru.unn.agile.RatioCalculator.viewmodel.ViewModel;

public class ViewModelWithTxtLoggerTests extends ViewModelTests {
    @Override
    public void setUp() {
        TxtLogger realLogger = new TxtLogger("./ViewModel_with_TxtLogger_Tests-lab3.log");
        super.setViewModel(new ViewModel(realLogger));
    }
}
