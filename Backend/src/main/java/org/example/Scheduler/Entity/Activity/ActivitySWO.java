package org.example.Scheduler.Entity.Activity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.Scheduler.Entity.LocationSWO;
import org.example.Scheduler.MetricCalculator.Metric1Calculator;
import org.example.Scheduler.MetricCalculator.Metric2Calculator;
import org.example.Scheduler.MetricCalculator.Metric3Calculator;
import org.example.Scheduler.Entity.TemporalIntervalSWO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivitySWO {

    private Integer id;
    private String name;

    private Integer durimin;
    private Integer durimax;
    private Integer duri;

    private Integer pi;
    private List<ActivityPartSWO> parts = new ArrayList<>();

    private Integer smini;
    private Integer smaxi;
    private Integer dmini;
    private Integer dmaxi;

    private List<TemporalIntervalSWO> F = new ArrayList<>();
    private List<LocationSWO> Loci = new ArrayList<>();

    private double difficulty;

    private Boolean isInterruptible = false;

    public ActivitySWO(int id, String name, int durimax, int durimin,
                       List<TemporalIntervalSWO> F, List<LocationSWO> Loci) {
        this.id = id;
        this.name = name;
        this.durimax = durimax;
        this.durimin = durimin;
        this.duri = 0;
        this.isInterruptible = false;
        this.dmini = 0;
        this.dmaxi = 0;
        this.smini = durimin;
        this.smaxi = durimax;
        this.F = F;
        this.Loci = Loci;
        this.pi = 0;
        this.parts = new ArrayList<>();
        calculateDifficulty();
    }

    public ActivitySWO(int id, String name, int durimax, int durimin, int smax, int smin,
                       int dmax, int dmin, List<TemporalIntervalSWO> F, List<LocationSWO> Loci) {
        this(id, name, durimax, durimin, F, Loci);
        this.isInterruptible = true;
        this.dmini = dmin;
        this.dmaxi = dmax;
        this.smini = smin;
        this.smaxi = smax;
    }

    public boolean isActivityDurationFeasable(int duration) {
        int A = durimin;
        int B = durimax;

        for (ActivityPartSWO a : getParts()) {
            A -= a.getDurij();
            B -= a.getDurij();
        }
        A -= duration;
        B -= duration;

        if (B == 0) return true;
        if (A < 0 && B < 0) return false;

        int k1 = (A - 1) / smaxi;
        int k2 = (B + 1) / smini + ((B + 1) % smini != 0 ? 1 : 0);
        return k1 + 1 != k2;
    }

    public void cleanUselessIntervals() {
        F.removeIf(t -> t.getWeight() < durimin);
    }

    public void calculateDifficulty() {
        difficulty = Math.max(
                Metric2Calculator.m2(this),
                Math.max(Metric3Calculator.m3(this), Metric1Calculator.m1(this))
        );
    }

    public List<Integer> getAllAvailableStartTimes(int duration) {
        List<Integer> availableStartTimes = new ArrayList<>();
        for (TemporalIntervalSWO t : F) {
            int lower = t.getAi();
            int upper = t.getBi() - duration;
            for (int i = lower; i <= upper; i++) {
                availableStartTimes.add(i);
            }
        }
        return availableStartTimes;
    }

    public void sortTemporalIntervalsByStart() {
        if (F != null) {
            F.sort(Comparator.comparingInt(TemporalIntervalSWO::getAi));
        }
    }

    public void sortActivityPartsByStart() {
        if (parts != null) {
            parts.sort(Comparator.comparingInt(ActivityPartSWO::getTij));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActivitySWO other)) return false;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ActivitySWO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", durimin=" + durimin +
                ", durimax=" + durimax +
                ", duri=" + duri +
                ", pi=" + pi +
                ", smini=" + smini +
                ", smaxi=" + smaxi +
                ", dmini=" + dmini +
                ", dmaxi=" + dmaxi +
                ", F=" + F +
                ", Loci=" + Loci +
                ", difficulty=" + difficulty +
                ", isInterruptible=" + isInterruptible +
                '}';
    }
}