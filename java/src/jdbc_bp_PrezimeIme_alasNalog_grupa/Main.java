package jdbc_bp_PrezimeIme_alasNalog_grupa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
	static {
		try {
			Class.forName("com.ibm.db2.jcc.DB2Driver");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static void main(String[] args) {
		String url = "jdbc:db2://localhost:50000/stud2020";

		try (
			Connection con = DriverManager.getConnection(url, "student", "abcdef");
			Scanner ulaz = new Scanner(System.in);
		) {
			con.setAutoCommit(false);
			
			Statement naredba = con.createStatement();
			naredba.execute("SET CURRENT LOCK TIMEOUT 5");
			
			try {
				aPronadjiSveKandidate(con);
				bUnesiPoeneSaTesta(con, ulaz);
				con.commit();
			} catch (Exception e) {
				con.rollback();
				throw e;
			} finally {
				con.setAutoCommit(true);
				naredba.execute("SET CURRENT LOCK TIMEOUT NULL");
				naredba.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(2);
		}
	}

	private static void aPronadjiSveKandidate(Connection con) throws SQLException {
		String sql = 
				"SELECT	    INDEKS, POENI_STUDIJE " + 
				"FROM 	    DA.PRAKSA " + 
				"WHERE 	    POENI_TEST = -1 " + 
				"ORDER BY   POENI_STUDIJE DESC";
		Statement naredba = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		ResultSet kursor = naredba.executeQuery(sql);
		
		boolean imaRedova;
		while(true) {
			try {
				imaRedova = kursor.next();
			} catch (SQLException e) {
				if (e.getErrorCode() == -911 || e.getErrorCode() == -913) {
					kursor.close();
					System.out.println("[FETCH] Objekat je zakljucan!");
					con.rollback();
					kursor = naredba.executeQuery(sql);
					continue;
				}
				throw e;
			}
			
			if (!imaRedova) {
				break;
			}
			
			Integer indeks = kursor.getInt(1);
			Float poeniStudije = kursor.getFloat(2);
			System.out.println(indeks + " " + poeniStudije);
		}
		
		kursor.close();
		naredba.close();
	}

	private static void bUnesiPoeneSaTesta(Connection con, Scanner ulaz) throws SQLException {
		boolean obradiNaredniIndeks = true;
		while (obradiNaredniIndeks) {
			System.out.println("Odaberite narednog kandidata iz liste:");
			Integer indeks = ulaz.nextInt();
			
			cUnesiPoeneNaTestuZaStudenta(con, indeks, ulaz);
			
			con.commit();
			
			System.out.println("Nastaviti dalje? [da/ne]");
			String odgovor = ulaz.next();
			obradiNaredniIndeks = odgovor.equalsIgnoreCase("da");
		}
	}

	private static void cUnesiPoeneNaTestuZaStudenta(Connection con, Integer indeks, Scanner ulaz) throws SQLException {
		String sql = 
				"UPDATE	DA.PRAKSA " +
				"SET	POENI_TEST = ? " +	
				"WHERE 	INDEKS = ?";
		PreparedStatement naredba = con.prepareStatement(sql);
		
		System.out.println("Unesite poene sa testa za kandidata sa indeksom: " + indeks);
		Float poeniTest = ulaz.nextFloat();
		
		naredba.setFloat(1, poeniTest);
		naredba.setInt(2, indeks);
		
		while (true) {
			try {
				naredba.executeUpdate();
			} catch (SQLException e) {
				if (e.getErrorCode() == -911 || e.getErrorCode() == -913) {
					System.out.println("[UPDATE] Objekat je zakljucan!");
					con.rollback();
					continue;
				}
				throw e;
			}
			break;
		}
		
		naredba.close();
	}
}
