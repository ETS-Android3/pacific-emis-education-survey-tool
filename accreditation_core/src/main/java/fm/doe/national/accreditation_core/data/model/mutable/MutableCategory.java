package fm.doe.national.accreditation_core.data.model.mutable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.accreditation_core.data.model.Category;
import fm.doe.national.accreditation_core.data.model.EvaluationForm;
import fm.doe.national.accreditation_core.data.model.ObservationInfo;
import fm.doe.national.accreditation_core.data.model.Standard;
import fm.doe.national.core.data.model.ConflictResolveStrategy;
import fm.doe.national.core.data.model.mutable.BaseMutableEntity;
import fm.doe.national.core.data.model.mutable.MutableProgress;
import fm.doe.national.core.utils.CollectionUtils;

public class MutableCategory extends BaseMutableEntity implements Category {

    private String title;
    private List<MutableStandard> standards;
    private MutableProgress progress = MutableProgress.createEmptyProgress();
    private EvaluationForm evaluationForm;
    @Nullable
    private MutableObservationInfo observationInfo;

    public MutableCategory(@NonNull Category other) {
        this.id = other.getId();
        this.title = other.getTitle();
        this.evaluationForm = other.getEvaluationForm();
        if (other.getStandards() != null) {
            this.standards = other.getStandards().stream().map(MutableStandard::new).collect(Collectors.toList());
        }

        final ObservationInfo otherObservationInfo = other.getObservationInfo();
        if (otherObservationInfo != null) {
            this.observationInfo = new MutableObservationInfo(otherObservationInfo);
        }
    }

    @NonNull
    @Override
    public String getTitle() {
        return title;
    }

    @NonNull
    @Override
    public List<MutableStandard> getStandards() {
        return standards;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStandards(List<MutableStandard> standards) {
        this.standards = standards;
    }

    @NonNull
    public MutableProgress getProgress() {
        return progress;
    }

    public void setProgress(MutableProgress progress) {
        this.progress = progress;
    }

    @Override
    public EvaluationForm getEvaluationForm() {
        return evaluationForm;
    }

    public void setEvaluationForm(EvaluationForm evaluationForm) {
        this.evaluationForm = evaluationForm;
    }

    public void setObservationInfo(@Nullable MutableObservationInfo observationInfo) {
        this.observationInfo = observationInfo;
    }

    @Nullable
    @Override
    public MutableObservationInfo getObservationInfo() {
        return observationInfo;
    }

    public List<MutableAnswer> merge(Category other, ConflictResolveStrategy strategy) {
        List<? extends Standard> externalStandards = other.getStandards();
        List<MutableAnswer> changedAnswers = new ArrayList<>();

        if (!CollectionUtils.isEmpty(externalStandards)) {
            for (Standard standard : externalStandards) {
                for (MutableStandard mutableStandard : getStandards()) {
                    if (mutableStandard.getSuffix().equals(standard.getSuffix())) {
                        changedAnswers.addAll(mutableStandard.merge(standard, strategy));
                        break;
                    }
                }
            }
        }

        final ObservationInfo otherObservationInfo = other.getObservationInfo();
        if (otherObservationInfo != null) {
            if (this.observationInfo == null) {
                this.observationInfo = new MutableObservationInfo(otherObservationInfo);
            } else {
                this.observationInfo.merge(otherObservationInfo, strategy);
            }
        }

        return changedAnswers;
    }
}
