package fm.doe.national.ui.screens.settings;

import android.content.ContentResolver;
import android.net.Uri;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.Arrays;

import fm.doe.national.BuildConfig;
import fm.doe.national.R;
import fm.doe.national.app_support.MicronesiaApplication;
import fm.doe.national.core.preferences.LocalSettings;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.domain.SettingsInteractor;
import fm.doe.national.ui.screens.settings.items.Item;
import fm.doe.national.ui.screens.settings.items.OptionsItemFactory;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class SettingsPresenter extends BasePresenter<SettingsView> {

    private final static String MIME_TYPE_SCHOOLS = "text/csv";

    private final SettingsInteractor interactor = MicronesiaApplication.getInjection().getAppComponent().getSettingsInteractor();
    private final LocalSettings localSettings = MicronesiaApplication.getInjection().getCoreComponent().getLocalSettings();
    private final OptionsItemFactory itemFactory = new OptionsItemFactory();

    public SettingsPresenter() {
        refresh();
    }

    private void refresh() {
        ArrayList<Item> items = new ArrayList<>(Arrays.asList(
                itemFactory.createLogoItem(),
                itemFactory.createPasswordItem(),
                itemFactory.createContextItem(localSettings.getAppRegion().getName()),
                itemFactory.createExportToExcelItem(localSettings.isExportToExcelEnabled()),
                itemFactory.createNameItem(localSettings.getAppName()),
                itemFactory.createContactItem(Text.from(localSettings.getContactName())),
                itemFactory.createOpModeItem(localSettings.getOperatingMode().getName()),
                itemFactory.createImportSchoolsItem(),
                itemFactory.createTemplatesItem()
        ));

        if (BuildConfig.DEBUG) {
            items.add(itemFactory.createDebugStorageItem());
            items.add(itemFactory.createDebugBuildInfoItem());
        }

        getViewState().setItems(items);
    }

    public void onItemPressed(Item item) {
        switch (item.getType()) {
            case DEBUG_STORAGE:
                onDebugStoragePressed();
                break;
            case CONTACT:
                onContactPressed();
                break;
            case CONTEXT:
                onContextPressed();
                break;
            case EXPORT_FOLDER:
                onChooseFolderPressed();
                break;
            case IMPORT_SCHOOLS:
                onImportSchoolsPressed();
                break;
            case LOGO:
                onLogoPressed();
                break;
            case OPERATING_MODE:
                onOperatingModePressed();
                break;
            case NAME:
                onNamePressed();
                break;
            case PASSWORD:
                onChangeMasterPasswordPressed();
                break;
            case TEMPLATES:
                onTemplatesPressed();
                break;
        }
    }

    public void onBooleanValueChanged(Item item, boolean value) {
        switch (item.getType()) {
            case EXPORT_TO_EXCEL:
                localSettings.setExportToExcelEnabled(value);
                break;
        }
    }

    private void onDebugStoragePressed() {
        interactor.showDebugStorage();
    }

    private void onLogoPressed() {
        getViewState().navigateToChangeLogo();
    }

    private void onContextPressed() {
        getViewState().showRegionSelector((region) -> {
            localSettings.setAppRegion(region);
            refresh();
        });
    }

    private void onNamePressed() {
        getViewState().showInputDialog(
                Text.from(R.string.title_input_name),
                localSettings.getAppName(),
                (name) -> {
                    localSettings.setAppName(name);
                    refresh();
                }
        );
    }

    private void onContactPressed() {
        getViewState().showInputDialog(
                Text.from(R.string.title_input_contact),
                Text.from(localSettings.getContactName()),
                (name) -> {
                    localSettings.setContactName(name);
                    refresh();
                }
        );
    }

    private void onOperatingModePressed() {
        getViewState().showOperatingModeSelector(mode -> {
            interactor.setOperatingMode(mode);
            refresh();
        });
    }

    private void onImportSchoolsPressed() {
        getViewState().openExternalDocumentsPicker(MIME_TYPE_SCHOOLS);
    }

    @Override
    public void onExternalDocumentPicked(ContentResolver contentResolver, Uri uri) {
        String content = readExternalUriToString(contentResolver, uri);
        if (content != null) {
            addDisposable(interactor.importSchools(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable -> getViewState().showWaiting())
                    .doFinally(() -> getViewState().hideWaiting())
                    .subscribe(() -> getViewState().showToast(Text.from(R.string.toast_import_schools_success)), this::handleError));
        }
    }

    private void onChooseFolderPressed() {
        addDisposable(interactor.selectExportFolder()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(this::refresh, this::handleError));
    }

    private void onTemplatesPressed() {
        getViewState().navigateToTemplates();
    }

    private void onChangeMasterPasswordPressed() {
        getViewState().navigateToChangePassword();
    }

}
