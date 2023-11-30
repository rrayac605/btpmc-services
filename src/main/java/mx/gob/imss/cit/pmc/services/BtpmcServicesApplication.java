package mx.gob.imss.cit.pmc.services;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BtpmcServicesApplication {

	public static void main(String[] args) {
		
		generaDigitoVerificador("0100831769");
		
	}
	
	
    public static Integer generaDigitoVerificador(String nss) {
        int suma = 0;
        int resultado = 0;
        for (int i = nss.length() - 1; i >= 1; i--) {
            if (i % 2 == 0) {
                int multiplicacion = (Integer.parseInt(nss.charAt(i - 1) + "")) * 2;
                if (multiplicacion > 9) {
                    suma = suma + ((multiplicacion - 10) + 1);
                } else {
                    suma = suma + multiplicacion;
                }
            } else {
                suma = suma + (Integer.parseInt(nss.charAt(i - 1) + ""));
            }
        }
        int modulo = suma % 10;
        if (modulo == 0) {
            resultado = 0;
        } else if (modulo < 10) {
            resultado = 10 - modulo;
        }
        System.out.print(resultado);
        return resultado;
    }

}
