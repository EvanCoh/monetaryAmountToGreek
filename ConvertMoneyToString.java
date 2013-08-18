
import java.math.BigDecimal;

/**
 * @author EvanCoh
 * se: ConvertMoneyToString.getVerbal(412444.87)
 */
public class ConvertMoneyToString {

    private ConvertMoneyToString() {
    }

    private static String[] m = {"", "ΕΝΑ ", "ΔΥΟ ", "ΤΡΙΑ ",
            "ΤΕΣΣΕΡΑ ", "ΠΕΝΤΕ ", "ΕΞΙ ",
            "ΕΠΤΑ ", "ΟΚΤΩ ", "ΕΝΝΕΑ "};
    private static String[] mF = {"", "ΜΙΑ ", "", "ΤΡΕΙΣ ",
            "ΤΕΣΣΕΡΙΣ "};
    private static String[] d1 = { //Διαφοροποίησεις των 11,12 ...
            "ΔΕΚΑ ", "ΕΝΤΕΚΑ ",
            "ΔΩΔΕΚΑ "};
    private static String[] d = {"", "ΔΕΚΑ", "ΕΙΚΟΣΙ ",
            "ΤΡΙΑΝΤΑ ", "ΣΑΡΑΝΤΑ ",
            "ΠΕΝΗΝΤΑ ", "ΕΞΗΝΤΑ ",
            "ΕΒΔΟΜΗΝΤΑ ", "ΟΓΔΟΝΤΑ ",
            "ΕΝΕΝΗΝΤΑ "};
    private static String[] e = {"", "ΕΚΑΤΟ", "ΔΙΑΚΟΣΙ",
            "ΤΡΙΑΚΟΣΙ", "ΤΕΤΡΑΚΟΣΙ",
            "ΠΕΝΤΑΚΟΣΙ", "ΕΞΑΚΟΣΙ",
            "ΕΠΤΑΚΟΣΙ", "ΟΚΤΑΚΟΣΙ",
            "ΕΝΝΙΑΚΟΣΙ"};
    private static String[] idx = {"ΛΕΠΤΑ", "ΕΥΡΩ ", "ΧΙΛΙΑΔΕΣ ",
            "ΕΚΑΤΟΜΜΥΡΙ", "ΔΙΣ", "ΤΡΙΣ",
            "ΤΕΤΡΑΚΙΣ ", "ΠΕΝΤΑΚΙΣ "};

    public static Integer Round(Double value) {
        Double doubl = Round(value, 0);
        Long lng = Math.round(doubl);
        return lng.intValue();
    }

    public static Double Round(Double value, Integer precision) {
        return new BigDecimal(String.valueOf(value)).setScale(precision, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static String GetVerbal(Double money) {
        return GetVerbal(money, true);
    }

    public static String GetVerbal(Double money, Boolean showZero) {
        return GetVerbal(money, showZero, true);
    }

    public static String GetVerbal(Double money, Boolean showZero,
                                   Boolean showCurrency) {
        String str;
        Short index = 0;
        Boolean isZero = true;
        Boolean isNegative = false;


        str = "";

        if (money < 0) {
            money = -money;
            isNegative = true;
        }

        if (money != Math.floor(money)) {

            Integer value = Round(100 * money - 100 * Math.floor(money));
            if (value >= 100) {
                value -= 100;
                money += 1.0;
            }

            //money = (Long)money;
            Long moneyLong = money.longValue();
            if (value > 0) {
                isZero = false;

                if (moneyLong >= 1 && value > 0) {
                    str += "ΚΑΙ ";
                }
                str += GetValue(value, index, showCurrency);
            }
        }

        while (money >= 1) {
            isZero = false;
            Double kati = money % 1000;
            Integer value = kati.intValue();


            money /= 1000;

            Integer indexValue = index.intValue();
            indexValue += 1;
            index = indexValue.shortValue();

            str = GetValue(value, index, showCurrency) + str;
            //money = (Long)money;

            Long moneyLong = money.longValue();
            money = moneyLong.doubleValue();

        }

        if (isZero) {
            if (showZero) {
                str = "ΜΗΔΕΝ ";
                if (showCurrency) {
                    str += idx[1];
                }
            }
        } else {
            if (isNegative) {
                str = "MEION " + str;
            }
        }

        return str;
    }

    public static String GetValue(Integer money, Short index,
                           Boolean showCurrency) {
        if (index == 2 && money == 1) {
            return "ΧΙΛΙΑ ";
        }

        String str = "";
        Integer dekmon = money % 100;
        Integer monades = dekmon % 10;
        Integer ekatontades = (Integer) (money / 100);
        Integer dekades = (Integer) (dekmon / 10);

        //EKATONTADES
        if (ekatontades == 1) {
            if (dekmon == 0) {
                str = e[1] + " ";
            } else {
                str = e[1] + "Ν ";
            }
        } else if (ekatontades > 1) {
            if (index == 2) {
                str = e[ekatontades] + "ΕΣ ";
            } else {
                str = e[ekatontades] + "Α ";
            }
        }

        //DEKADES
        switch (dekmon) {
            case 10:
                str += d1[monades];    //"ΔΕΚΑ " με κενό στο τέλος
                break;
            case 11:
                str += d1[monades];
                monades = 0;
                break;
            case 12:
                str += d1[monades];
                monades = 0;
                break;
            default:
                str += d[dekades];
                break;
        }

        //MONADES
        if ((index == 2)
                && (monades == 1 || monades == 3 || monades == 4)) {
            str += mF[monades];
        } else {
            if (dekmon < 10 || dekmon > 12) {
                str += m[monades];
            }
        }

        if (str.length() > 0 || index == 1) {
            if (index == 0 && money == 1) {
                if (showCurrency) {
                    str += "ΛΕΠΤΟ";
                }
            } else {
                if (index > 1 || showCurrency) {
                    str += idx[index];
                    if (index > 2) {
                        if (index > 3) {
                            str += idx[3];
                        }
                        if (money > 1) {
                            str += "Α ";
                        } else {
                            str += "Ο ";
                        }
                    }
                }
            }
        }

        return str;
    }
}
