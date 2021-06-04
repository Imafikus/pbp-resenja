package hibernate_bp_PrezimeIme_alasNalog_grupa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="DA.PRAKSA")
public class Praksa {
	@Id
	private Integer indeks;
	
	@Column(name = "POENI_STUDIJE", nullable = false)
	private Float poeniSaStudija;
	
	@Column(name = "POENI_TEST", nullable = false)
	private Float poeniSaTesta;
	
	@OneToOne
	@JoinColumn(name = "INDEKS", referencedColumnName = "INDEKS", insertable = false, updatable = false)
	private Student student;

	public Integer getIndeks() {
		return indeks;
	}

	public Float getPoeniSaStudija() {
		return poeniSaStudija;
	}

	public Float getPoeniSaTesta() {
		return poeniSaTesta;
	}
}
