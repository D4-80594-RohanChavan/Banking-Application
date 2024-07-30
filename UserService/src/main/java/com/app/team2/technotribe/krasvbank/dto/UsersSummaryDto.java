package com.app.team2.technotribe.krasvbank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UsersSummaryDto {
private int activeUsers;
private int inActiveUsers;
private int TotalUsers;

}
