package net.skytrail.ilcdb.domain;

import gorm.transform.Entity
import java.text.SimpleDateFormat
import java.util.EnumSet

@Entity
class StatusAchieved {
    static belongsTo = [ client:Client ]

    enum Type {
        Any("Any"),
        Citizenship("Citizenship"),
        LPR("LPR"),
        DACA("DACA"),
        TPS("TPS"),
        LPRConditionsRemoved("LPR Conditions Removed"),
        LPRCardRenewed("LPR Card Renewed"),
        I90("I-90"),
        EOIR("EOIR"),
        FOIA("FOIA"),
        I102("I-102"),
        I129F("I-129F"),
        I130IR("I-130 (IR)"),
        I130nonIR("I-130 (non-IR)"),
        I131("I-131"),
        I192("I-192"),
        I360("I-360"),
        I360VAWA("I-360 VAWA"),
        I360VAWAderivative("I-360 VAWA derivative"),
        I539("I-539"),
        I539VVisa("I-539 (V-Visa)"),
        I601("I-601"),
        I751("I-751"),
        I765("I-765"),
        I821("I-821 (TPS)"),
        I824("I-824"),
        I881("I-881 (NACARA)"),
        I912("I-912"),
        I914("I-914"),
        I914SuppA("I-914 Supp A"),
        I918("I-918"),
        I918SuppA("I-918 Supp A"),
        I918SuppB("I-918 Supp B"),
        I929("I-929"),
        N336("N-336"),
        N400("N-400"),
        N565("N-565"),
        N600("N-600"),
        AOS("AOS"),
        AOS_OneStep("AOS one-step"),
        AOS_T("AOS T"),
        AOS_U("AOS U"),
        AOS_VAWA("AOS VAWA"),
        CP("Consular Processing"),
        CP_T("Consular Processing T"),
        CP_U("Consular Processing U"),
        CP_Admin_Processing("CP Admin Processing"),
        CP_I601_Waiver("CP I-601 Waiver"),
        CP_I601A_Provisional_Waiver("CP I601A Provisional Waiver")

        private final String value;

        Type(String value) {
            this.value = value;
        }

        public String toString() {
            return value;
        }

        static list() {
            [Any, Citizenship, DACA, LPR, LPRCardRenewed, LPRConditionsRemoved, TPS, I90, EOIR, FOIA,
             I102, I129F, I130IR, I130nonIR, I131, I192, I360, I360VAWA, I360VAWAderivative, I539,
             I539VVisa, I601, I751, I765, I821, I824, I881, I912, I914, I914SuppA, I918, I918SuppA,
             I918SuppB, I929, N336, N400, N565, N600, AOS, AOS_OneStep, AOS_T, AOS_U, AOS_VAWA,
             CP, CP_T, CP_U, CP_Admin_Processing, CP_I601_Waiver, CP_I601A_Provisional_Waiver]
        }

        public static Type fromValue(String aStatusTypeValue) {
            for (Type statusType : EnumSet.allOf(Type.class)) {
                if (aStatusTypeValue.equalsIgnoreCase(statusType.toString()))
                    return statusType;
            }
            return null;
        }
    };

    Type type
    Date date

    static constraints = {
    }

    static transients = ["statusAchievedDateString"]

    String getStatusAchievedDateString() {
        if (date instanceof Date)
            return (new SimpleDateFormat("yyyy-MM-dd").format(date));
        return "";
    }

    public String toString() {
        return "${type}: ${getStatusAchievedDateString()}";
    }
}
