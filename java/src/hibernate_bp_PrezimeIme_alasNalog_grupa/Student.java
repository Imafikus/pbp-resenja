package hibernate_bp_PrezimeIme_alasNalog_grupa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="DA.DOSIJE")
public class Student {
	@Id
	private Integer indeks;

	@Column(name = "IME", nullable = false)
	private String ime;

	@Column(name = "PREZIME", nullable = false)
	private String prezime;

	@ManyToOne
	@JoinColumn(name = "IDPROGRAMA", referencedColumnName = "ID", insertable = false, updatable = false)
	private StudijskiProgram studijskiProgram;

	@OneToOne(mappedBy = "student")
	private Praksa kandidat;

	public Integer getIndeks() {
		return indeks;
	}

	public String getIme() {
		return ime;
	}

	public String getPrezime() {
		return prezime;
	}
}
