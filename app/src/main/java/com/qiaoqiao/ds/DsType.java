package com.qiaoqiao.ds;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.qiaoqiao.ds.DsType.CAMERA;
import static com.qiaoqiao.ds.DsType.LOCAL;
import static com.qiaoqiao.ds.DsType.WEB;


@IntDef({ WEB,
          LOCAL,
          CAMERA })
@Retention(RetentionPolicy.SOURCE)
public @interface DsType {
	int WEB = 0x00000000;
	int LOCAL = 0x00000004;
	int CAMERA = 0x00000006;

}