package guru.sfg.brewery.domain.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Authority {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  private String permission;

  @ManyToMany(mappedBy = "authorities")
  private Set<Role> roles;
}
