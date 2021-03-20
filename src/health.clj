(ns health)

(def basal-energy-expenditure-coefficients
  {:male [13.75M 5M 6.76M 66.5M]
   :female [9.56M 1.85M 4.68M 66.5M]})

(def activity-levels
  {:low 1.53M
   :medium 1.76M
   :high 2.25M})

(def objectives
  {:lose -400M
   :gain 300M
   :define 0M})

(defn calculate-bmi [weight height]
  (with-precision 4 (/ weight (* height height))))

(defn basal-energy-expenditure [weight height age gender]
  (let [[c1 c2 c3 c4] (gender basal-energy-expenditure-coefficients)]
    (+ (* c1 weight)
       (* c2 (* height 100M))
       (* c3 age)
       c4)))

(defn total-energy-expenditure [activity-level & {:keys [bee]}]
  (* (activity-level activity-levels)
     bee))

(defn calories-calc [objective & {:keys [tee]}]
  (+ (objective objectives)
     tee))

(defn calculate-values
  [{:keys [age weight height gender activity-level objective]}]
  (as-> {} result
      (assoc result :bmi (calculate-bmi weight height))
      (assoc result :bee (basal-energy-expenditure weight height age gender))
      (assoc result :tee (total-energy-expenditure activity-level result))
      (assoc result :calories (calories-calc objective result))))

(comment
  (calculate-values
    {:age 25M
     :weight 95M
     :height 1.75M
     :gender :male
     :activity-level :medium
     :objective :gain})
    ;; => {:bmi 31.02M, :bee 2416.75M, :tee 4253.4800M, :calories 4553.4800M}
  )
