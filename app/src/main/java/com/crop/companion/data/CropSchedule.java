package com.crop.companion.data;

import androidx.room.Ignore;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor(onConstructor = @__({@Ignore}))
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class CropSchedule {
    public Crop crop;
    public LocalDate firstPlanting;
    public List<Integer> weeklyPlantingAmounts;
}
