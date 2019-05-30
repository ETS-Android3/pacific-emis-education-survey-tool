package fm.doe.national.accreditation.ui.survey.navigation;

import androidx.annotation.NonNull;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.core.R;
import fm.doe.national.core.ui.screens.base.BaseFragment;
import fm.doe.national.report.ui.report.ReportFragment;

public class ReportNavigationItem implements NavigationItem {

    private static final Text HEADER = Text.from(R.string.title_report_short);
    private static final Text NAME = Text.from(R.string.title_report);

    @NonNull
    @Override
    public BaseFragment buildFragment() {
        return new ReportFragment();
    }

    @Override
    public Text getName() {
        return NAME;
    }

    @Override
    public long getHeaderId() {
        return Long.MAX_VALUE;
    }

    @Override
    public Text getHeader() {
        return HEADER;
    }

}
