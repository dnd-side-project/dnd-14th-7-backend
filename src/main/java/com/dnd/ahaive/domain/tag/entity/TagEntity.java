package com.dnd.ahaive.domain.tag.entity;

import com.dnd.ahaive.domain.user.entity.User;
import com.dnd.ahaive.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TagEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  @Column(name = "tag_entity_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  private String tagName;

  private String colorCode;

  @Builder
  private TagEntity(User user, String tagName) {
    this.user = user;
    this.tagName = tagName;
    this.colorCode = generateRandomColorCode();
  }

  public static TagEntity of(User user, String tagName) {
    return TagEntity.builder()
        .user(user)
        .tagName(tagName)
        .build();
  }

  private static String generateRandomColorCode() {
    int hue = (int) (Math.random() * 360);
    int saturation = 60 + (int) (Math.random() * 30);
    int lightness = 45 + (int) (Math.random() * 20);

    float hf = hue / 360f, sf = saturation / 100f, lf = lightness / 100f;
    float q = lf < 0.5f ? lf * (1 + sf) : lf + sf - lf * sf;
    float p = 2 * lf - q;

    float[] rgb = new float[3];
    float[] offsets = {hf + 1f/3, hf, hf - 1f/3};
    for (int i = 0; i < 3; i++) {
      float t = offsets[i];
      if (t < 0) t += 1;
      if (t > 1) t -= 1;
      if (t < 1f/6) rgb[i] = p + (q - p) * 6 * t;
      else if (t < 1f/2) rgb[i] = q;
      else if (t < 2f/3) rgb[i] = p + (q - p) * (2f/3 - t) * 6;
      else rgb[i] = p;
    }

    return String.format("#%02X%02X%02X", (int)(rgb[0]*255), (int)(rgb[1]*255), (int)(rgb[2]*255));
  }

  public boolean containsTag(String tagName) {
    return this.tagName.equals(tagName);
  }




}
