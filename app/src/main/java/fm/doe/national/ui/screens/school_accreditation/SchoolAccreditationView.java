package fm.doe.national.ui.screens.school_accreditation;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.data.data_source.models.SchoolAccreditationPassing;
import fm.doe.national.ui.screens.menu.drawer.BaseDrawerView;

@StateStrategyType(AddToEndSingleStrategy.class)
interface SchoolAccreditationView extends BaseDrawerView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToCategoryChooser(long passingId);


    void setAccreditations(List<SchoolAccreditationPassing> accreditations);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showSurveyDeleteConfirmation();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void removeSurveyPassing(SchoolAccreditationPassing passing);
}
