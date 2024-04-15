package fintechservice.communication.model;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "indexes")
@Getter
@Setter
@NoArgsConstructor

public class Index {

	public Index(String index, LocalDate date, double open, double high, double low, double close, double adjClose,
			int volume) {
		this.index = index;
		this.date = date;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.adjClose = adjClose;
		this.volume = volume;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "index")
	private String index;

	@Column(name = "date")
	private LocalDate date;

	@Column(name = "open")
	private double open;

	@Column(name = "high")
	private double high;

	@Column(name = "low")
	private double low;

	@Column(name = "close")
	private double close;

	@Column(name = "adj_close")
	private double adjClose;

	@Column(name = "volume")
	private int volume;

	@Override
	public int hashCode() {
		return Objects.hash(date, index);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Index other = (Index) obj;
		return Objects.equals(date, other.date) && Objects.equals(index, other.index);
	}
}
