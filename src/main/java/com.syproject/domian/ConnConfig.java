package com.syproject.domian;

import com.syproject.common.Jackson;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author:lee
 * @Date:2018/7/18
 * @description:数据库信息
 */
@Entity
@Table(name = "plt_conn_config")
@Setter
@Getter
@ToString
public class ConnConfig {

  @Id
  @Column(name = "conn_name")
  private String connName;

  @Column(name = "db_name")
  private String dbName;

  @Column(name = "db_host")
  private String dbHost;

  @Column(name = "db_user")
  private String dbUser;

  @Column(name = "db_pass")
  private String dbPass;

}
