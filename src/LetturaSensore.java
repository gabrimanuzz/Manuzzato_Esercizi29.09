import java.net.Inet4Address;
import java.util.Optional;

import static java.lang.IO.println;

public class LetturaSensore {

    private Double temperatura;
    private Integer umiditàPercentuale;
    private Long timeStampUnix;
    private  Boolean batteriaScarica;

    public LetturaSensore(Double temperatura, Integer umiditàPercentuale, Long timeStampUnix, Boolean batteriaScarica) {
        this.temperatura = temperatura;
        this.umiditàPercentuale = umiditàPercentuale;
        this.timeStampUnix = timeStampUnix;
        this.batteriaScarica = batteriaScarica;
    }

    public Double getTemperatura() { return temperatura; }
    public Integer getUmiditàPercentuale() { return umiditàPercentuale; }
    public Long getTimeStampUnix() { return timeStampUnix; }
    public Boolean getBatteriaScarica() { return batteriaScarica; }

    public static Optional<LetturaSensore> parsePacchetto(String raw) {
        if (raw == null || raw.isEmpty()) {
            println("ERRORE! Stringa vuota");
            return Optional.empty();
        }

        Double temp =null;
        Integer umid = null;
        Long ts = null;
        Boolean battLow = null;

        String[] valori =  raw.split(";");

        for (String coppia : valori) {
            String[] chiaveValore =  coppia.split("=");
            if (chiaveValore.length != 2) continue;

            String chiave = chiaveValore[0];
            String valore = chiaveValore[1];

            switch (chiave) {
                case "temp":
                    try {
                        temp = Double.valueOf(valore);
                    } catch (NumberFormatException e) {
                        println("ERRORE! campo temperatura corroto");
                    }
                    break;
                case "umid":
                    try {
                        int parsedUmid = Integer.parseInt(valore);
                        if (parsedUmid < 0 || parsedUmid > 100) {
                            throw new InvalidInputException("Umidità fuori range: " + parsedUmid);
                        }
                        umid = parsedUmid;
                    } catch (NumberFormatException e) {
                        println("ERRORE! Campo umid corrotto");
                    }
                    break;

                case "ts":
                    try {
                        ts = Long.valueOf(valore);
                    } catch (NumberFormatException e) {
                        println("ERRORE! Campo ts corrotto");
                    }
                    break;
                case "batt_low":
                    battLow = Boolean.valueOf(valore);
                    break;
                default:
                    break;
            }
        }

        return Optional.of(new LetturaSensore(temp, umid, ts, battLow));
    }

    @Override
    public String toString() {
        return "LetturaSensore{" +
                "temperatura=" + temperatura +
                ", umiditaPercentuale=" + umiditàPercentuale +
                ", timestampUnix=" + timeStampUnix +
                ", batteriaScarica=" + batteriaScarica +
                '}';
    }

}
