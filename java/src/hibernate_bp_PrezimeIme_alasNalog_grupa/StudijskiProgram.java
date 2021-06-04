package hibernate_bp_PrezimeIme_alasNalog_grupa;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="DA.STUDIJSKIPROGRAM")
public class StudijskiProgram {
	@Id
	private int id;

	@Column(nullable = false)
	private String naziv;
	
	@Column(nullable = false)
    private Integer obimespb;

	@OneToMany(mappedBy = "studijskiProgram")
	private List<Student> studenti;

	public int getId() {
		return id;
	}

	public String getNaziv() {
		return naziv;
	}

	public Integer getObimespb() {
		return obimespb;
	}

	public List<Student> getStudenti() {
		return studenti;
	}
	
	
}
