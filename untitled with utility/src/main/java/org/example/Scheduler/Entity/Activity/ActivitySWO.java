package org.example.Scheduler.Entity.Activity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.Scheduler.Entity.LocationSWO;
import org.example.Scheduler.Entity.MetricCalculator.Metric1Calculator;
import org.example.Scheduler.Entity.MetricCalculator.Metric2Calculator;
import org.example.Scheduler.Entity.MetricCalculator.Metric3Calculator;
import org.example.Scheduler.Entity.TemporalIntervalSWO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivitySWO
{
    private Integer id;
    private String name;

    //duration range
    private Integer durimin;
    private Integer durimax;

    //actual duration
    private Integer duri;

    //pi is the number of parts
    private Integer pi;
    private List<ActivityPartSWO> parts;

    //minimum and maximum allowed part duration
    private Integer smini;
    private Integer smaxi;

    // minimum and maximum allowed distance between parts
    private Integer dmini;
    private Integer dmaxi;

    private List<TemporalIntervalSWO> F;
    private List<LocationSWO> Loci;

    public double difficulty;
    public double currentUtility;

    public Boolean troublemaker;
    public Boolean scheduled;
    public Boolean isInterruptible;

    public ActivitySWO(int id, String name, int durimax, int durimin, List<TemporalIntervalSWO> F, List<LocationSWO> Loci)
    {
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

    public ActivitySWO(int id, String name, int durimax, int durimin, int smax, int smin, int dmax, int dmin, List<TemporalIntervalSWO> F, List<LocationSWO> Loci)
    {
        this.id = id;
        this.name = name;
        this.durimax = durimax;
        this.durimin = durimin;
        this.duri = 0;
        this.isInterruptible = true;
        this.dmini = dmin;
        this.dmaxi = dmax;
        this.smini = smin;
        this.smaxi = smax;
        this.F = F;
        this.Loci = Loci;
        this.pi = 0;
        this.parts = new ArrayList<>();
        calculateDifficulty();
    }

    public boolean isActivityDurationFeasable(int duration)
    {
        int A = durimin;
        int B = durimax;
        System.out.println("Initial a: " + A);
        System.out.println("Initial b: " + B);
        for(ActivityPartSWO a : getParts())
        {
            A-=a.getDurij();
            B-=a.getDurij();
        }
        A-=duration;
        B-=duration;
        System.out.println("Final a: " + A);
        System.out.println("Final b: " + B);
        if(B == 0)
            return true;
        if(A < 0 && B < 0)
            return false;

        int k1 = (A-1) / smaxi;
        int k2 = (B+1) / smini + ((B+1)%smini != 0 ? 1 : 0);
        System.out.println("k1: " + k1);
        System.out.println("k2: " + k2);
        return k1 + 1 != k2;
    }

    public void cleanUselessIntervals()
    {
        for(TemporalIntervalSWO t : getF())
        {
            if(t.getWeight() < durimin)
                getF().remove(t);
        }
    }

    public void calculateDifficulty()
    {
        difficulty = Math.max(Metric2Calculator.m2(this), Math.max(Metric3Calculator.m3(this), Metric1Calculator.m1(this)));
    }

    public List<Integer> getAllAvailableStartTimes(int duration)
    {
        List<Integer> availableStartTimes = new ArrayList<>();
        for(TemporalIntervalSWO t : getF())
        {
            int lower = t.getAi();
            int upper = t.getBi() - duration;
            for(int i = lower; i<= upper; i++)
                availableStartTimes.add(i);
        }
        return availableStartTimes;
    }

    public void sortTemporalIntervalsByStart()
    {
        if (F != null) {
            F.sort((a, b) -> Integer.compare(a.getAi(), b.getAi()));
        }
    }

    public void sortActivityPartsByStart()
    {
        if (parts != null) {
            parts.sort((a1, a2) -> Integer.compare(a1.getTij(), a2.getTij()));
        }
    }

    @Override public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof ActivitySWO other)) return false;
        return Objects.equals(id, other.id);
    }
    @Override public int hashCode()
    {
        return Objects.hash(id);
    }
}