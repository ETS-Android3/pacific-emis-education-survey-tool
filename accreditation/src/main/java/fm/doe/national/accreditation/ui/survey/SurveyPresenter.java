package fm.doe.national.accreditation.ui.survey;

import com.omegar.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.accreditation.ui.survey.navigation.ProgressableNavigationItem;
import fm.doe.national.accreditation.ui.survey.navigation.ReportNavigationItem;
import fm.doe.national.accreditation.ui.survey.navigation.SchoolAccreditationNavigationItem;
import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.core.interactors.SurveyInteractor;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.accreditation.ui.survey.navigation.NavigationItem;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class SurveyPresenter extends BasePresenter<SurveyView> {

    private final SurveyInteractor surveyInteractor;

    public SurveyPresenter(CoreComponent coreComponent) {
        surveyInteractor = coreComponent.getSurveyInteractor();

        getViewState().setSchoolName(surveyInteractor.getCurrentSurvey().getSchoolName());
        addDisposable(
                surveyInteractor.requestCategories()
                        .flatMap(categories -> Single.fromCallable(() -> {
                            List<NavigationItem> navigationItems = new ArrayList<>();
                            categories.forEach(category -> category.getStandards().forEach(standard -> {
                                navigationItems.add(new SchoolAccreditationNavigationItem(category, standard));
                            }));
                            navigationItems.add(new ReportNavigationItem());
                            return navigationItems;
                        }))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::setNavigationItems, this::handleError)
        );
    }

    public void onNavigationItemPressed(NavigationItem item) {
        if (item instanceof ProgressableNavigationItem) {
            ProgressableNavigationItem navigationItem = (ProgressableNavigationItem) item;
            getViewState().setNavigationTitle(navigationItem.getNamePrefix(), navigationItem.getName(), navigationItem.getProgress());
        } else {
            getViewState().setNavigationTitle(null, item.getName(), null);
        }
        getViewState().showNavigationItem(item);
    }
}
