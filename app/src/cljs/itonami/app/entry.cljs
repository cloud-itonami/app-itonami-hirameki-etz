(ns itonami.app.entry
  "Hirameki (etz) on the device (AWAI-3502). Calls hirameki.methods.analyze
  unchanged. Patent harvest (network), dataset materialization and the CID
  helpers are not used. The example fields and assignees are fictional."
  (:require [hirameki.methods.analyze :as a]
            [itonami.app.run :as run]))

(def ^:private example
  "{:fields
 [{:id \"F01\" :name \"Example field: power semiconductors\" :cpc-section :H :cpc-subclass \"H01L\"
   :assignees [[:assignee-a 38] [:assignee-b 22] [:assignee-c 6] [:other 34]]
   :essentiality 0.8 :expired-share 0.2 :expiring-soon-share 0.15 :open-license-share 0.05}
  {:id \"F02\" :name \"Example field: soil sensors\" :cpc-section :G :cpc-subclass \"G01N\"
   :assignees [[:assignee-d 9] [:assignee-e 7] [:other 84]]
   :essentiality 0.2 :expired-share 0.55 :expiring-soon-share 0.2 :open-license-share 0.3}]
 :patents
 [{:id \"P1\" :title \"Example patent\" :jurisdiction :jp :field \"F01\" :assignee :assignee-a
   :status :granted :filing-year 2009 :term-years 20}]
 :ref-year 2026}")

(run/register!
 {:name "analyze" :label "分野を分析する"
  :help "分野ごとに最大の出願人の占有率・HHI・チョークポイントの危険度・解放の進み具合・排他の負荷を計算し、de-monopolization / release / open-license / monitor のどれに回すかを示します。特許は満了までの年数を数えます。"
  :example example
  :run (fn [{:keys [fields patents ref-year]}] (a/analyze {:fields fields :patents patents} (or ref-year 2026)))})

(run/register!
 {:name "report" :label "報告を作る"
  :help "同じ分析から Markdown の報告と、CPC の部ごとの網羅状況を作ります。"
  :example example
  :run (fn [{:keys [fields patents ref-year]}]
         (let [an (a/analyze {:fields fields :patents patents} (or ref-year 2026))]
           (a/render-report an (a/coverage an))))})
