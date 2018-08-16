package fm.doe.national.ui.adapters;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.doe.national.R;
import fm.doe.national.mock.MockCriteria;
import fm.doe.national.mock.MockSubCriteria;
import fm.doe.national.util.ViewUtils;

public class CriteriaAdapter extends RecyclerView.Adapter<CriteriaAdapter.CriteriaViewHolder> {

    private List<MockCriteria> items;

    public CriteriaAdapter() {
        super();
        items = new ArrayList<>();
    }

    @NonNull
    @Override
    public CriteriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CriteriaViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_criteria, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CriteriaViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setCriterias(@NonNull List<MockCriteria> criterias) {
        items = criterias;
        notifyDataSetChanged();
    }

    protected class CriteriaViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textview_criteria_title)
        TextView titleTextView;

        @BindView(R.id.textview_progress)
        TextView progressTextView;

        @BindView(R.id.progressbar)
        ProgressBar progressBar;

        @BindView(R.id.recyclerview_subcriterias)
        OmegaRecyclerView subcriteriasRecycler;

        @BindView(R.id.constraintlayout_criteria_header)
        View header;

        protected CriteriaViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        protected void bind(MockCriteria criteria) {
            titleTextView.setText(criteria.getName());

            SubCriteriaAdapter adapter = new SubCriteriaAdapter(criteria.getSubcriterias(), () -> {
                rebindProgress(criteria.getSubcriterias());
            });
            subcriteriasRecycler.setAdapter(adapter);

            rebindProgress(criteria.getSubcriterias());

            header.setOnClickListener((View v) -> {
                if (subcriteriasRecycler.getVisibility() == View.VISIBLE) {
                    ViewUtils.animateCollapsing(subcriteriasRecycler);
                } else {
                    ViewUtils.animateExpanding(subcriteriasRecycler);
                }
            });
        }

        private void rebindProgress(@NonNull List<MockSubCriteria> subCriterias) {
            int totalQuestions = subCriterias.size();
            int answeredQuestions = 0;
            for (MockSubCriteria subCriteria: subCriterias) {
                if (subCriteria.getState() != MockSubCriteria.State.NOT_ANSWERED) answeredQuestions++;
            }
            progressTextView.setText(String.format(Locale.US, "%d/%d", answeredQuestions, totalQuestions));

            int progress = totalQuestions > 0 ? (int)((float)answeredQuestions / totalQuestions * 100) : 0;

            if (Build.VERSION.SDK_INT > 24) {
                progressBar.setProgress(progress, true);
            } else {
                progressBar.setProgress(progress);
            }
        }
    }
}
