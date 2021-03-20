(ns health)

(def basal-energy-expenditure-infos
  {:male [13.75 5 6.76 66.5]
   :female [9.56 1.85 4.68 66.5]})

(def activity-levels
  {:low 1.53
   :medium 1.76
   :high 2.25})

(def objectives
  {:lose -400
   :gain 300
   :define 0})

(defn calculate-bmi [weight height]
  (with-precision 4 (/ weight (* height height))))

(defn basal-energy-expenditure [weight height age gender]
  (let [[c1 c2 c3 c4] (gender basal-energy-expenditure-infos)]
    (+ (* c1 weight)
       (* c2 (* height 100))
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
  )
