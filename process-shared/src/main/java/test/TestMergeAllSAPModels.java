/*
 * Copyright © 2009-2014 The Apromore Initiative.
 *
 * This file is part of “Apromore”.
 *
 * “Apromore” is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * “Apromore” is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.
 * If not, see <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package test;

import graph.Graph;
import merge.MergeModels;

import common.EPCModelParser;

public class TestMergeAllSAPModels {

	static String allEPCsFile = "models/AllEPCs.epml";
	static String result_prefix = "models/mergedepcs/";
	static String[] models = new String[] {"1An_ka9y", "1An_kazo", "1An_kc5k", "1An_kefi", "1An_kex6", 
					"1An_kfye", "1An_kg4m", "1An_kgnx", "1An_khe0", "1An_kiki", "1An_kjdg", "1An_kjk6", 
					"1An_kkkz", "1An_klol", "1An_km48", "1An_kmmd", "1An_kmy0", "1An_knwl", "1An_kr8w", 
					"1An_ks1c", "1An_ks6c", "1An_kv7b", "1An_kynn", "1An_kyxa", "1An_kzoq", "1An_l0zn", 
					"1An_l1y8", "1An_l2cf", "1An_l51v", "1An_l62m", "1An_l6i9", "1An_l6tr", "1An_l75y", 
					"1An_l7f4", "1An_l7kw", "1An_l7u8", "1An_l89b", "1An_l8wo", "1An_lac3", "1An_lanf", 
					"1An_laym", "1An_lbl5", "1An_lc1c", "1Ar_m7re", "1Ar_m86y", "1Ar_m8ew", "1Ar_m8hl",
					"1Ar_m8qw", "1Ar_ma2i", "1Be_1y63", "1Be_1yk5", "1Be_204a", "1Be_22v7", "1Be_25fz", 
					"1Be_25my", "1Be_2aze", "1Be_2ft2", "1Be_2fyv", "1Be_2gm6", "1Be_2k7e", "1Be_2kiu", 
					"1Be_2n6s", "1Be_2n9n", "1Be_2ork", "1Be_2rd6", "1Be_2rh8", "1Be_2rxu", "1Be_2sc7", 
					"1Be_2skz", "1Be_2tnc", "1Be_2vbl", "1Be_2wup", "1Be_2xk1", "1Be_30t8", "1Be_310v", 
					"1Be_322n", "1Be_32fe", "1Be_343s", "1Be_34is", "1Be_38qs", "1Be_394s", "1Be_3a62", 
					"1Be_3bub", "1Be_3e7i", "1Be_3era", "1Be_3j4l", "1Be_8r9o", "1Be_8ri3", "1Be_8uyu", 
					"1En_cmrh", "1En_cmvz", "1En_cnx7", "1En_co6p", "1En_cpez", "1En_cpi3", "1En_cpo4", 
					"1En_cq52", "1En_cqab", "1En_cqec", "1En_cqu6", "1En_cqx0", "1En_cr6d", "1En_crae", 
					"1En_csa4", "1En_cstn", "1En_cthd", "1En_ctjd", "1En_cuik", "1Er_h4fo", "1Er_h4x3", 
					"1Er_h5oa", "1Er_h8hr", "1Er_hct3", "1Er_hhi9", "1Er_hsc3", "1Er_hsto", "1Er_i6yw", 
					"1Er_i9qm", "1Er_icx1", "1Er_ieku", "1Er_iif2", "1Er_iqc9", "1Er_ixgh", "1Er_ixso", 
					"1Er_j49j", "1Er_j5sl", "1Er_j6vb", "1Ex_dwdy", "1Ex_dxa3", "1Ex_dy43", "1Ex_dyea", 
					"1Ex_dzq9", "1Ex_e1oz", "1Ex_e334", "1Ex_e43l", "1Ex_e58p", "1Ex_e5ew", "1Ex_e5k6", 
					"1Ex_e5xq", "1Ex_e61x", "1Ex_e68v", "1Ex_e6dx", "1Ex_e6n6", "1Ex_e76a", "1Ex_e7kd", 
					"1Ex_e8d2", "1Ex_e8ik", "1Ex_e8vj", "1Ex_ea41", "1Ex_eal7", "1Ex_eazz", "1Ex_ebbl", 
					"1Ex_ec3f", "1Ex_ecum", "1Ex_eczr", "1Ex_egln", "1Ex_eh68", "1Ex_ehmr", "1Ex_ei2s", 
					"1Ex_eifk", "1Ex_eiqj", "1Ex_ej4e", "1Ex_ek3t", "1Ex_elth", "1Ex_emr6", "1Ex_eoxo",
					"1Ex_epa1", "1Ex_epnw", "1Ex_epz4", "1Ex_eq92", "1Ex_eqm0", "1Ex_eqw8", "1Ex_erws",
					"1Ex_esd0", "1Ex_eu1x", "1Ex_eugx", "1Ex_evsj", "1Ex_exkt", "1Ex_expg", "1Ex_exvr",
					"1Ex_ey8p", "1Im_lcbm", "1Im_lcn5", "1Im_lgn9", "1Im_lhqh", "1Im_ljm4", "1Im_lmu3",
					"1In_agyu", "1In_ahnr", "1In_ajc5", "1In_ajlf", "1In_aklk", "1In_anvm", "1In_aoee",
					"1In_aoot", "1In_apbf", "1In_aslk", "1In_at4y", "1In_avfd", "1In_avon", "1In_awpb", 
					"1In_azzd", "1In_b0n0", "1In_b19m", "1In_b1vj", "1In_b2pt", "1In_b2z3", "1In_b3z3", 
					"1In_b72q", "1In_b7s7", "1In_b8et", "1In_bb6y", "1In_bc12", "1In_bcx8", "1In_bd6i", 
					"1In_be6n", "1In_bhaa", "1In_bht2", "1In_bi2g", "1In_bip2", "1In_blhp", "1In_bmd6", 
					"1Ku_8w3g", "1Ku_8w9x", "1Ku_8y3g", "1Ku_903f", "1Ku_91bx", "1Ku_9335", "1Ku_93jr", 
					"1Ku_96oz", "1Ku_97uj", "1Ku_9bjf", "1Ku_9do6", "1Ku_9e6t", "1Ku_9efq", "1Ku_9h14", 
					"1Ku_9jfq", "1Ku_9l6o", "1Ku_9mgu", "1Ku_9nk6", "1Ku_9ojw", "1Ku_9p00", "1Ku_9rnu", 
					"1Ku_9soy", "1Ku_9vyx", "1Ku_9yyx", "1Ku_9zhk", "1Ku_9zq8", "1Ku_a0t4", "1Ku_a1xs", 
					"1Ku_a4cg", "1Ku_a6af", "1Ku_a6uv", "1Ku_a813", "1Ku_a8h7", "1Ku_aa4c", "1Ku_acul", 
					"1Ku_add8", "1Ku_adlw", "1Ku_afas", "1Ku_agcd", "1Ku_agg3", "1Ku_agow", "1Or_lojl", 
					"1Or_lp0i", "1Or_lp64", "1Or_lpud", "1Or_lqhg", "1Pe_lrja", "1Pe_lrsy", "1Pe_lshs", 
					"1Pe_lsq3", "1Pe_lsz3", "1Pe_ltjp", "1Pe_ltrk", "1Pe_lu4d", "1Pe_luqh", "1Pe_lv0n", 
					"1Pe_lvyh", "1Pe_lweq", "1Pe_lwqx", "1Pe_lx1m", "1Pe_lxhj", "1Pe_ly3r", "1Pe_lz60", 
					"1Pe_lzyg", "1Pe_m0q9", "1Pe_m11m", "1Pe_m1h0", "1Pe_m1rw", "1Pe_m2ho", "1Pe_max4", 
					"1Pe_mbsh", "1Pe_mcfu", "1Pe_mcyk", "1Pe_mdap", "1Pe_mdi2", "1Pe_me7s", "1Pe_meqq", 
					"1Pe_mew4", "1Pe_mg24", "1Pe_mg84", "1Pe_mgei", "1Pe_mhix", "1Pe_mhtm", "1Pe_mi2b", 
					"1Pe_mi5q", "1Pe_mi8f", "1Pe_mib4", "1Pe_mie0", "1Pr_10om", "1Pr_18wm", "1Pr_19op", 
					"1Pr_1bt1", "1Pr_1c2v", "1Pr_1gm8", "1Pr_1j5k", "1Pr_1kwn", "1Pr_1mso", "1Pr_1n9b", 
					"1Pr_1nf9", "1Pr_1nhz", "1Pr_1nor", "1Pr_1o9q", "1Pr_1p3a", "1Pr_1p5s", "1Pr_1qq7",
					"1Pr_1sct", "1Pr_1sgy", "1Pr_1vyc", "1Pr_33k-", "1Pr_3nmw", "1Pr_3nuo", "1Pr_3qr1", 
					"1Pr_3t2p", "1Pr_3wae", "1Pr_3wiu", "1Pr_422x", "1Pr_43cw", "1Pr_43om", "1Pr_46ft", 
					"1Pr_474u", "1Pr_47ww", "1Pr_4ar4", "1Pr_4bas", "1Pr_4bgy", "1Pr_4ddy", "1Pr_4dw9", 
					"1Pr_7og-", "1Pr_aag-", "1Pr_afh-", "1Pr_cuxy", "1Pr_cv1j", "1Pr_cwo2", "1Pr_cx0o", 
					"1Pr_cx7a", "1Pr_d09l", "1Pr_d16t", "1Pr_d1ur", "1Pr_d2p8", "1Pr_d45l", "1Pr_d4fv", 
					"1Pr_d6dk", "1Pr_d957", "1Pr_da70", "1Pr_dbx-", "1Pr_dc0n", "1Pr_dcid", "1Pr_dcqc", 
					"1Pr_dfri", "1Pr_dgbj", "1Pr_dgrn", "1Pr_diw6", "1Pr_djki", "1Pr_dk0k", "1Pr_dkfa", 
					"1Pr_dl73", "1Pr_dmqb", "1Pr_dn32", "1Pr_dotx", "1Pr_dpvq", "1Pr_dq8m", "1Pr_dqw8", 
					"1Pr_drf4", "1Pr_ds05", "1Pr_dtjg", "1Pr_dufm", "1Pr_dve4", "1Pr_eg0-", "1Pr_eru-", 
					"1Pr_f7e-", "1Pr_fbh-", "1Pr_fx--", "1Pr_h6u-", "1Pr_htc-", "1Pr_je6-", "1Pr_jfy-", 
					"1Pr_kj--", "1Pr_lp7-", "1Pr_m00-", "1Pr_mpk-", "1Pr_nwb-", "1Pr_qik-", "1Pr_qrx-", 
					"1Pr_rkx-", "1Pr_smx-", "1Qu_bmwp", "1Qu_bniq", "1Qu_br2k", "1Qu_bs1c", "1Qu_btq3",
					"1Qu_bxuo", "1Qu_byan", "1Qu_bywg", "1Qu_c117", "1Qu_c3ie", "1Qu_c5we", "1Qu_c6ew",
					"1Qu_c7do", "1Qu_c8yd", "1Qu_cb8m", "1Qu_cdn2", "1Qu_ce0j", "1Qu_ch64", "1Qu_ci6z",
					"1Qu_cjnw", "1Re_n1pd", "1Tr_ez2f", "1Tr_ez9m", "1Tr_f0f4", "1Tr_f0xm", "1Tr_f66p",
					"1Tr_f6bx", "1Tr_f6lf", "1Tr_f6wa", "1Tr_f7g6", "1Tr_f9a4", "1Tr_f9ft", "1Tr_f9sw",
					"1Tr_fy8q", "1Tr_fyhp", "1Tr_g2hu", "1Tr_g42t", "1Tr_g4ax", "1Tr_g563", "1Tr_g5h8",
					"1Tr_g5zr", "1Tr_g68b", "1Tr_g7sk", "1Tr_g7vi", "1Tr_g866", "1Tr_ga4d", "1Tr_gabg",
					"1Tr_gc22", "1Tr_gcc2", "1Tr_gd86", "1Tr_gdht", "1Tr_gepc", "1Tr_gezc", "1Tr_gfmu",
					"1Tr_gh06", "1Tr_gh7g", "1Tr_gi0l", "1Tr_gifu", "1Tr_gjb3", "1Tr_gjiw", "1Tr_gkss",
					"1Tr_gl74", "1Tr_gm2d", "1Tr_gm9n", "1Tr_gn7l", "1Tr_gnli", "1Tr_gogd", "1Tr_gppj",
					"1Tr_gq9g", "1Un_j73d", "1Un_j7jq", "1Un_j86j", "1Un_japr", "1Un_jdpj", "1Un_jh6h",
					"1Un_jqw9", "1Un_juwn", "1Un_jwkf", "1Un_jxpf", "1Un_jyvy", "1Un_jz68", "1Un_jzgs", 
					"1Un_k1qh", "1Un_k30q", "1Un_k54y", "1Un_k5qb", "1Un_k6b1", "1Un_k7jq", "1Un_k89c", 
					"1Un_k964", "1Un_k9yk", "1Ve_4fbt", "1Ve_4fin", "1Ve_4geh", "1Ve_4gw1", "1Ve_4hbk", 
					"1Ve_4jca", "1Ve_4jln", "1Ve_4k75", "1Ve_4mai", "1Ve_4mua", "1Ve_4mxc", "1Ve_4ose", 
					"1Ve_4q66", "1Ve_4qka", "1Ve_4xoy", "1Ve_4ymf", "1Ve_4zsw", "1Ve_512s", "1Ve_52tx", 
					"1Ve_531s", "1Ve_57p5", "1Ve_58l9", "1Ve_5a31", "1Ve_5dvr", "1Ve_5e1y", "1Ve_5i83", 
					"1Ve_5jtb", "1Ve_5kzj", "1Ve_5otm", "1Ve_5p60", "1Ve_5tcy", "1Ve_5vna", "1Ve_5x4o", 
					"1Ve_5ycw", "1Ve_6294", "1Ve_62rl", "1Ve_68lb", "1Ve_6a59", "1Ve_6bms", "1Ve_6dlt", 
					"1Ve_6lp9", "1Ve_6m1w", "1Ve_6mnb", "1Ve_6mxu", "1Ve_6n9a", "1Ve_6s89", "1Ve_6u91", 
					"1Ve_6vyf", "1Ve_6wdf", "1Ve_701w", "1Ve_70sp", "1Ve_710u", "1Ve_7180", "1Ve_7647", 
					"1Ve_77z0", "1Ve_7btr", "1Ve_7c1w", "1Ve_7coq", "1Ve_7if9", "1Ve_7kcl", "1Ve_7lu4", 
					"1Ve_7n23", "1Ve_7rr4", "1Ve_7s3r", "1Ve_7sma", "1Ve_7suf", "1Ve_7uuo", "1Ve_7vev", 
					"1Ve_80w9", "1Ve_82t3", "1Ve_84am", "1Ve_85il", "1Ve_8a7d", "1Ve_8ak0", "1Ve_8b2j", 
					"1Ve_8bao", "1Ve_m2sp", "1Ve_m31t", "1Ve_m3ae", "1Ve_m3da", "1Ve_m3i8", "1Ve_m40o", 
					"1Ve_m47b", "1Ve_m4a0", "1Ve_m4hd", "1Ve_m4y2", "1Ve_m5jn", "1Ve_m5og", "1Ve_m5rc", 
					"1Ve_m63t", "1Ve_m6az", "1Ve_m6i5", "1Ve_m6rs", "1Ve_m75y", "1Ve_musj", "1Ve_mvdh", 
					"1Ve_mvwz", "1Ve_mw7t", "1Ve_mx29", "1Ve_mxed", "1Ve_my35", "1Ve_mz1m", "1Ve_mzcb", 
					"1Ve_n0ge", "1Ve_n0ve", "1Ve_n0z8", "1Wa_nuwn"};
	
	public static String[] modelPairs = new String[] {
		"1Er_h4x3","1Un_j7jq",
		"1Tr_gkss","1Tr_gn7l",
		"1Tr_gi0l","1Tr_gkss",
		"1In_apbf","1In_b8et",
		"1Be_3j4l","1Ve_6wdf",
		"1Be_34is","1Ve_6wdf",
		"1Be_2aze","1Ve_6wdf",
		"1Ve_7s3r","1Ve_8ak0",
		"1Ve_6m1w","1Ve_8ak0",
		"1In_agyu","1In_b19m",
		"1Pr_3t2p","1Pr_4ar4",
		"1In_avon","1In_bd6i",
		"1In_avon","1Ku_96oz",
		"1In_avon","1Ku_9rnu",
		"1In_ajlf","1In_avon",
		"1In_b8et","1In_bip2",
		"1In_apbf","1In_bip2",
		"1Er_hhi9","1Un_jh6h",
		"1Qu_bs1c","1Qu_ci6z",
		"1In_b1vj","1In_blhp",
		"1Er_hct3","1Un_jdpj",
		"1Tr_gnli","1Tr_gq9g",
		"1Tr_gl74","1Tr_gq9g",
		"1Tr_gifu","1Tr_gq9g",
		"1Tr_gcc2","1Tr_gifu",
		"1Tr_gcc2","1Tr_gl74",
		"1Tr_gcc2","1Tr_gnli",
		"1Tr_f0xm","1Tr_gcc2",
		"1Ku_9efq","1Ku_a1xs",
		"1Ve_7c1w","1Ve_7uuo",
		"1Ve_7coq","1Ve_7vev",
		"1In_be6n","1Ku_9soy",
		"1In_b3z3","1Ku_9soy",
		"1In_awpb","1In_b3z3",
		"1In_awpb","1In_be6n",
		"1In_aklk","1In_b3z3",
		"1In_aklk","1In_be6n",
		"1In_b2z3","1In_bd6i",
		"1In_b2z3","1Ku_96oz",
		"1In_b2z3","1Ku_9rnu",
		"1In_ajlf","1In_b2z3",
		"1An_kmmd","1An_kynn",
		"1An_kmy0","1An_kyxa",
		"1Un_juwn","1Un_k6b1",
		"1An_ks6c","1An_l2cf",
		"1Tr_gh06","1Tr_gm2d",
		"1Tr_gcc2","1Tr_gezc",
		"1Tr_gcc2","1Tr_gq9g",
		"1Be_204a","1Ve_6u91",
		"1Ku_97uj","1Ku_9soy",
		"1In_awpb","1Ku_97uj",
		"1In_aklk","1Ku_97uj",
		"1Tr_f0xm","1Tr_gifu",
		"1Tr_f0xm","1Tr_gl74",
		"1Tr_f0xm","1Tr_gnli",
		"1In_avon","1In_b2z3",
		"1Ve_7lu4","1Ve_84am",
		"1Ve_5x4o","1Ve_7lu4",
		"1Be_2rxu","1Be_322n",
		"1Qu_btq3","1Qu_cjnw",
		"1In_bc12","1Ku_9nk6",
		"1An_kkkz","1An_l0zn",
		"1Ve_7647","1Ve_7if9",
		"1Ve_7647","1Ve_80w9",
		"1Ve_6s89","1Ve_7if9",
		"1Ve_6s89","1Ve_80w9",
		"1Tr_gjb3","1Tr_gm2d",
		"1Tr_gh06","1Tr_gjb3",
		"1Tr_gezc","1Tr_gifu",
		"1Tr_gezc","1Tr_gl74",
		"1Tr_gezc","1Tr_gnli",
		"1An_kr8w","1An_l0zn",
		"1Er_h5oa","1Un_j86j",
		"1Ve_5i83","1Ve_68lb",
		"1Pr_1gm8","1Pr_1qq7",
		"1Ku_93jr","1Ku_9p00",
		"1Tr_f0xm","1Tr_gezc",
		"1Tr_f0xm","1Tr_gq9g",
		"1Ku_91bx","1Ku_a4cg",
		"1In_be6n","1Ku_97uj",
		"1In_b3z3","1Ku_97uj",
		"1Ku_903f","1Ku_9mgu",
		"1Ve_7suf","1Ve_8bao",
		"1Qu_c7do","1Qu_ci6z",
		"1Ve_5kzj","1Ve_5ycw",
		"1Tr_gezc","1Tr_gq9g",
		"1In_bi2g","1Pr_3t2p",
		"1In_bi2g","1Pr_4ar4",
		"1In_b7s7","1Pr_3t2p",
		"1In_b7s7","1Pr_4ar4",
		"1In_b0n0","1Pr_3t2p",
		"1In_b0n0","1Pr_4ar4",
		"1In_aoot","1Pr_3t2p",
		"1In_aoot","1Pr_4ar4",
		"1Be_394s","1Ve_701w",
		"1Be_1yk5","1Ve_701w",
		"1An_kkkz","1An_kr8w",
		"1In_at4y","1In_b8et",
		"1In_apbf","1In_at4y",
		"1Pr_1j5k","1Pr_1sgy",
		"1Tr_gn7l","1Tr_gppj",
		"1Tr_gi0l","1Tr_gppj",
		"1In_at4y","1In_bip2",
		"1Tr_gkss","1Tr_gppj",
		"1Qu_bs1c","1Qu_c7do",
		"1En_crae","1En_ctjd",
		"1An_klol","1An_l1y8",
		"1Qu_btq3","1Qu_ce0j",
		"1Tr_g5h8","1Tr_gezc",
		"1Qu_ce0j","1Qu_cjnw",
		"1In_agyu","1In_bb6y",
		"1Er_h4fo","1Un_j73d",
		"1Ve_6n9a","1Ve_8bao",
		"1Be_32fe","1Be_3bub",
		"1Be_22v7","1Be_3bub",
		"1Ve_6bms","1Ve_7lu4",
		"1Ve_68lb","1Ve_6s89",
		"1Ve_68lb","1Ve_7647",
		"1En_cr6d","1En_cthd",
		"1Be_3a62","1Ve_6u91",
		"1Be_2tnc","1Ve_6u91",
		"1In_b19m","1In_bb6y",
		"1Ve_4zsw","1Ve_7lu4",
		"1Ve_5kzj","1Ve_77z0",
		"1An_khe0","1An_lbl5",
		"1Tr_gepc","1Tr_gi0l",
		"1Tr_gepc","1Tr_gn7l",
		"1Tr_gc22","1Tr_gi0l",
		"1Tr_gc22","1Tr_gn7l",
		"1Tr_g563","1Tr_gi0l",
		"1Tr_g563","1Tr_gn7l",
		"1Pr_1qq7","1Pr_1vyc",
		"1Pr_1gm8","1Pr_1vyc",
		"1Tr_g866","1Tr_gifu",
		"1Tr_g866","1Tr_gnli",
		"1Tr_g866","1Tr_gl74",
		"1En_csa4","1En_cuik",
		"1Ve_5ycw","1Ve_7n23",
		"1Ve_5ycw","1Ve_85il",
		"1Tr_gdht","1Tr_gfmu",
		"1Qu_cb8m","1Qu_cjnw",
		"1Tr_gepc","1Tr_gkss",
		"1Tr_gc22","1Tr_gkss",
		"1Tr_g563","1Tr_gkss",
		"1Ve_5tcy","1Ve_68lb",
		"1Ve_5kzj","1Ve_7n23",
		"1Ve_5kzj","1Ve_85il",
		"1Tr_f66p","1Tr_f6bx",
		"1Tr_f0xm","1Tr_g866",
		"1Ku_9zhk","1Ku_add8",
		"1Ku_9e6t","1Ku_add8",
		"1Be_2wup","1Be_343s",
		"1Be_25my","1Ve_6vyf",
		"1Ku_9nk6","1Qu_byan",
		"1Tr_g866","1Tr_gq9g",
		"1Tr_ez2f","1Tr_gh06",
		"1Tr_ez2f","1Tr_gm2d",
		"1An_knwl","1An_kzoq",
		"1An_kc5k","1An_l8wo",
		"1Ve_6bms","1Ve_84am",
		"1Ve_5ycw","1Ve_77z0",
		"1Ve_5x4o","1Ve_6bms",
		"1Ve_4zsw","1Ve_6bms",
		"1Tr_ga4d","1Tr_gjb3",
		"1Tr_g866","1Tr_gcc2",
		"1Tr_g5h8","1Tr_gcc2",
		"1Be_204a","1Be_2tnc",
		"1Be_204a","1Be_3a62",
		"1Be_1y63","1Be_2sc7",
		"1Ex_ei2s","1Ex_epz4",
		"1An_ka9y","1An_l7u8",
		"1Ve_77z0","1Ve_7n23",
		"1Ve_77z0","1Ve_85il",
		"1Be_2sc7","1Be_30t8",
		"1Pr_3wae","1Pr_3wiu",
		"1Tr_ez2f","1Tr_gjb3",
		"1Ve_6294","1Ve_7c1w",
		"1Ve_4zsw","1Ve_5x4o",
		"1Ve_4zsw","1Ve_84am",
		"1Ve_6294","1Ve_7uuo",
		"1Ve_5i83","1Ve_6s89",
		"1Ve_5i83","1Ve_7647",
		"1In_bc12","1Qu_byan",
		"1Be_343s","1Ve_6vyf",
		"1Be_2vbl","1Be_32fe",
		"1Be_22v7","1Be_2vbl",
		"1An_kg4m","1An_lanf",
		"1In_aslk","1In_b19m",
		"1In_agyu","1In_aslk",
		"1Ve_68lb","1Ve_7if9",
		"1Ve_68lb","1Ve_80w9",
		"1Tr_g5h8","1Tr_gifu",
		"1Tr_g5h8","1Tr_gl74",
		"1Tr_g5h8","1Tr_gnli",
		"1In_b8et","1Ku_a1xs",
		"1In_apbf","1Ku_a1xs",
		"1Be_2kiu","1Be_3bub",
		"1Qu_btq3","1Qu_cb8m",
		"1Ve_52tx","1Ve_5dvr",
		"1Tr_ga4d","1Tr_gh06",
		"1Tr_ga4d","1Tr_gm2d",
		"1Ve_6n9a","1Ve_7suf",
		"1In_bip2","1Ku_a1xs",
		"1Tr_g4ax","1Tr_gdht",
		"1Ve_5jtb","1Ve_6a59",
		"1Ve_5jtb","1Ve_7kcl",
		"1Ve_5jtb","1Ve_82t3",
		"1Ve_5jtb","1Ve_5vna",
		"1Ve_58l9","1Ve_5jtb",
		"1Ve_4ymf","1Ve_6a59",
		"1Ve_4ymf","1Ve_58l9",
		"1Ve_4ymf","1Ve_7kcl",
		"1Ve_4ymf","1Ve_82t3",
		"1Ve_4ymf","1Ve_5vna",
		"1Pr_fbh-","1Pr_htc-",
		"1Ve_5i83","1Ve_5tcy",
		"1Tr_f0xm","1Tr_g5h8",
		"1Pr_10om","1Pr_smx-",
		"1Ve_5vna","1Ve_6a59",
		"1Ve_5vna","1Ve_7kcl",
		"1Ve_5vna","1Ve_82t3",
		"1Ve_58l9","1Ve_5vna",
		"1Tr_g866","1Tr_gezc",
		"1Tr_g5h8","1Tr_gq9g",
		"1Tr_ez2f","1Tr_ga4d",
		"1Qu_bywg","1Qu_cjnw",
		"1In_b8et","1Ku_9efq",
		"1In_apbf","1Ku_9efq",
		"1Ve_57p5","1Ve_5i83",
		"1Ve_57p5","1Ve_68lb",
		"1Qu_cb8m","1Qu_ce0j",
		"1In_at4y","1Ku_a1xs",
		"1In_bip2","1Ku_9efq",
		"1Tr_gh7g","1Tr_gm9n",
		"1Ve_musj","1Ve_mvwz",
		"1Tr_gepc","1Tr_gppj",
		"1Tr_gc22","1Tr_gppj",
		"1Tr_g563","1Tr_gppj",
		"1Ve_5tcy","1Ve_6s89",
		"1Ve_5tcy","1Ve_7647",
		"1Qu_bywg","1Qu_cb8m",
		"1Tr_g5zr","1Tr_gh06",
		"1Tr_g5zr","1Tr_gm2d",
		"1Ku_8w9x","1Ku_a6uv",
		"1Pe_lx1m","1Pe_ly3r",
		"1Be_1y63","1Be_38qs",
		"1In_ahnr","1In_b1vj",
		"1Qu_c117","1Qu_c3ie",
		"1In_at4y","1Ku_9efq",
		"1Qu_btq3","1Qu_bywg",
		"1Ve_5i83","1Ve_7if9",
		"1Ve_5i83","1Ve_80w9",
		"1Tr_gjiw","1Tr_gogd",
		"1Tr_g5zr","1Tr_gjb3",
		"1Pe_mg84","1Pe_mgei",
		"1Pe_lrja","1Pe_lshs",
		"1Ku_agcd","1Ku_agg3",
		"1Ku_add8","1Ve_6mnb",
		"1Ku_add8","1Ve_70sp",
		"1Ku_add8","1Ve_7btr",
		"1Ku_add8","1Ve_7sma",
		"1Ku_add8","1Ve_8b2j",
		"1Ku_9zhk","1Ve_6mnb",
		"1Ku_9zhk","1Ve_70sp",
		"1Ku_9zhk","1Ve_7btr",
		"1Ku_9zhk","1Ve_7sma",
		"1Ku_9zhk","1Ve_8b2j",
		"1Ku_9p00","1Ku_a8h7",
		"1Ku_9e6t","1Ve_6mnb",
		"1Ku_9e6t","1Ve_70sp",
		"1Ku_9e6t","1Ve_7btr",
		"1Ku_9e6t","1Ve_7sma",
		"1Ku_9e6t","1Ve_8b2j",
		"1In_bcx8","1Ku_9335",
		"1In_bcx8","1Ku_9ojw",
		"1In_bcx8","1Ku_a813",
		"1In_b2pt","1Ku_9335",
		"1In_b2pt","1Ku_9ojw",
		"1In_b2pt","1Ku_a813",
		"1In_avfd","1Ku_9335",
		"1In_avfd","1Ku_9ojw",
		"1In_avfd","1Ku_a813",
		"1In_ajc5","1Ku_9335",
		"1In_ajc5","1Ku_9ojw",
		"1In_ajc5","1Ku_a813",
		"1In_ahnr","1In_blhp",
		"1Ex_ehmr","1Ex_epnw",
		"1Be_2skz","1Be_394s",
		"1Be_2kiu","1Ve_7lu4",
		"1Be_25my","1Be_2wup",
		"1Be_1yk5","1Be_2skz",
		"1Be_32fe","1Ve_4zsw",
		"1Be_22v7","1Ve_4zsw",
		"1Qu_bywg","1Qu_ce0j",
		"1Ve_4fbt","1Ve_4geh",
		"1Ex_eal7","1Ex_ebbl",
		"1Pr_1gm8","1Pr_1j5k",
		"1Be_2gm6","1Ve_77z0",
		"1Be_310v","1Ve_701w",
		"1Be_2vbl","1Be_3bub",
		"1Tr_g4ax","1Tr_gfmu",
		"1Pr_3nuo","1Pr_43om",
		"1Pr_3nmw","1Pr_43cw",
		"1Tr_g42t","1Tr_gd86",
		"1Ve_m40o","1Ve_m4a0",
		"1Ve_5tcy","1Ve_7if9",
		"1Ve_5tcy","1Ve_80w9",
		"1Ve_5otm","1Ve_6294",
		"1Ve_5e1y","1Ve_5p60",
		"1In_azzd","1In_b72q",
		"1In_azzd","1In_bhaa",
		"1In_azzd","1Ku_a0t4",
		"1In_anvm","1In_azzd",
		"1Be_2skz","1Ve_701w",
		"1Be_2kiu","1Be_32fe",
		"1Be_22v7","1Be_2kiu",
		"1Pe_m0q9","1Pe_m1rw",
		"1Ku_9bjf","1Ku_9vyx",
		"1Ku_9bjf","1Ku_aa4c",
		"1In_aslk","1In_bb6y",
		"1Be_2sc7","1Be_38qs",
		"1An_kazo","1An_l89b",
		"1Tr_ez2f","1Tr_f0xm",
		"1Be_3era","1In_anvm",
		"1Be_3era","1In_b72q",
		"1Be_3era","1In_bhaa",
		"1Be_3era","1Ku_a0t4",
		"1Be_2xk1","1Be_34is",
		"1Be_2xk1","1Be_3j4l",
		"1Be_2aze","1Be_2xk1",
		"1Be_1y63","1Be_30t8",
		"1Tr_f6wa","1Tr_f7g6",
		"1Pr_1sgy","1Pr_1vyc",
		"1Be_2xk1","1Ve_6wdf",
		"1Ve_6dlt","1Ve_7n23",
		"1Ve_6dlt","1Ve_85il",
		"1Tr_gm2d","1Tr_gnli",
		"1Tr_gl74","1Tr_gm2d",
		"1Tr_gifu","1Tr_gm2d",
		"1Tr_gh06","1Tr_gifu",
		"1Tr_gh06","1Tr_gl74",
		"1Tr_gh06","1Tr_gnli",
		"1Tr_g42t","1Tr_gh06",
		"1Tr_g42t","1Tr_gm2d",
		"1Tr_ez2f","1Tr_g5zr",
		"1Ku_agcd","1Ku_agow",
		"1Be_2wup","1Ve_6vyf",
		"1Be_25my","1Be_343s",
		"1An_l6i9","1An_l7kw",
		"1Tr_g42t","1Tr_g5h8",
		"1Tr_ez2f","1Tr_gcc2",
		"1Tr_gd86","1Tr_gh06",
		"1Tr_gd86","1Tr_gm2d",
		"1Ve_5dvr","1Ve_5otm",
		"1En_cq52","1En_cqab",
		"1Tr_gjiw","1Tr_gm9n",
		"1Tr_gh7g","1Tr_gjiw",
		"1Ku_9l6o","1Qu_c7do",
		"1Be_3bub","1Ve_7lu4",
		"1Tr_gm2d","1Tr_gq9g",
		"1Tr_gjb3","1Tr_gl74",
		"1Tr_gjb3","1Tr_gnli",
		"1Tr_gifu","1Tr_gjb3",
		"1Tr_gh06","1Tr_gq9g",
		"1Tr_g42t","1Tr_gjb3",
		"1Ku_93jr","1Ku_a8h7",
		"1Ve_57p5","1Ve_6s89",
		"1Ve_57p5","1Ve_7647",
		"1Ve_4xoy","1Ve_57p5",
		"1Tr_gd86","1Tr_gezc",
		"1Tr_gd86","1Tr_gjb3",
		"1Tr_f0xm","1Tr_gh06",
		"1Tr_f0xm","1Tr_gm2d",
		"1Tr_ez2f","1Tr_gifu",
		"1Tr_ez2f","1Tr_gl74",
		"1Tr_ez2f","1Tr_gnli",
		"1Tr_ez2f","1Tr_g42t",
		"1Pr_1qq7","1Pr_1sgy",
		"1Pr_1gm8","1Pr_1sgy",
		"1Pe_lrsy","1Pe_lsq3",
		"1Ku_a813","1Qu_br2k",
		"1Ku_a813","1Qu_c6ew",
		"1Ku_a813","1Qu_ch64",
		"1Ku_9ojw","1Qu_br2k",
		"1Ku_9ojw","1Qu_c6ew",
		"1Ku_9ojw","1Qu_ch64",
		"1Ku_9335","1Qu_br2k",
		"1Ku_9335","1Qu_c6ew",
		"1Ku_9335","1Qu_ch64",
		"1In_bcx8","1Qu_br2k",
		"1In_bcx8","1Qu_c6ew",
		"1In_bcx8","1Qu_ch64",
		"1In_b2pt","1Qu_br2k",
		"1In_b2pt","1Qu_c6ew",
		"1In_b2pt","1Qu_ch64",
		"1In_avfd","1Qu_br2k",
		"1In_avfd","1Qu_c6ew",
		"1In_avfd","1Qu_ch64",
		"1In_ajc5","1Qu_br2k",
		"1In_ajc5","1Qu_c6ew",
		"1In_ajc5","1Qu_ch64",
		"1En_cmvz","1En_ctjd",
		"1Be_3bub","1Ve_4zsw",
		"1Be_310v","1Be_394s",
		"1Be_2skz","1Be_310v",
		"1Be_2rd6","1Be_2rxu",
		"1Be_2kiu","1Ve_5x4o",
		"1Be_2kiu","1Ve_84am",
		"1Be_1yk5","1Be_310v",
		"1Or_lojl","1Or_lqhg",
		"1Ku_91bx","1Ku_afas",
		"1Tr_gjb3","1Tr_gq9g",
		"1Tr_g5zr","1Tr_ga4d",
		"1Tr_ez2f","1Tr_gd86",
		"1Ve_5ycw","1Ve_6dlt",
		"1Ve_mxed","1Ve_mzcb",
		"1Tr_gcc2","1Tr_gh06",
		"1Tr_gcc2","1Tr_gm2d",
		"1Tr_g5zr","1Tr_g866",
		"1Tr_f0xm","1Tr_gjb3",
		"1Tr_ez2f","1Tr_gezc",
		"1Tr_ez2f","1Tr_gq9g",
		"1Be_3bub","1Ve_6bms",
		"1Ve_57p5","1Ve_5tcy",
		"1Pr_1j5k","1Pr_1qq7",
		"1Pr_1bt1","1Pr_1sct",
		"1Pe_m0q9","1Pe_m1h0",
		"1En_cr6d","1En_crae",
		"1Be_2ft2","1Be_2fyv",
		"1Ve_5kzj","1Ve_6dlt",
		"1Pr_1sct","1Pr_1sgy",
		"1Ve_5otm","1Ve_7c1w",
		"1Be_2kiu","1Ve_6bms",
		"1Ex_egln","1Ex_eoxo",
		"1Ve_52tx","1Ve_5otm",
		"1Pe_ltrk","1Pe_lvyh",
		"1Tr_gcc2","1Tr_gjb3",
		"1Tr_g42t","1Tr_ga4d",
		"1Qu_bxuo","1Qu_byan",
		"1Ex_elth","1Ex_erws",
		"1En_cmrh","1En_cr6d",
		"1Tr_ga4d","1Tr_gd86",
		"1In_bb6y","1Ku_9mgu",
		"1Pr_3qr1","1Pr_47ww",
		"1Ve_5p60","1Ve_7180",
		"1Ve_5otm","1Ve_7uuo",
		"1Tr_ez9m","1Tr_gh7g",
		"1Tr_ez9m","1Tr_gm9n",
		"1Pr_4bas","1Pr_4ddy",
		"1Ve_6mxu","1Ve_710u",
		"1Pe_mhix","1Pe_mi2b",
		"1Be_2gm6","1Ve_5kzj",
		"1Ve_710u","1Ve_7180",
		"1Tr_g866","1Tr_gh06",
		"1Tr_g866","1Tr_gm2d",
		"1Tr_gezc","1Tr_gh06",
		"1Tr_gezc","1Tr_gm2d",
		"1Qu_bmwp","1Qu_bniq",
		"1Ve_5otm","1Ve_710u",
		"1Tr_g42t","1Tr_gezc",
		"1Ku_903f","1Ku_a6af",
		"1Pr_dkfa","1Pr_dl73",
		"1Pr_d1ur","1Pr_djki",
		"1En_co6p","1En_cstn",
		"1Ve_5otm","1Ve_5p60",
		"1Ve_5dvr","1Ve_710u",
		"1Ve_57p5","1Ve_7if9",
		"1Ve_57p5","1Ve_80w9",
		"1Ve_4fbt","1Ve_4fin",
		"1Tr_gezc","1Tr_gjb3",
		"1Tr_g866","1Tr_gjb3",
		"1Tr_g5h8","1Tr_g866",
		"1Tr_f66p","1Tr_f6lf",
		"1Tr_f0xm","1Tr_ga4d",
		"1Pr_1j5k","1Pr_1vyc",
		"1Pe_lrja","1Pe_lsq3",
		"1Ku_a6uv","1Ve_4fbt",
		"1Ex_epz4","1Ex_exkt",
		"1Ex_ei2s","1Ex_exkt",
		"1En_cthd","1En_ctjd",
		"1En_cmvz","1En_crae",
		"1En_cmrh","1En_cmvz",
		"1Be_3bub","1Ve_5x4o",
		"1Be_3bub","1Ve_84am",
		"1Be_2k7e","1Ex_eiqj",
		"1Be_2k7e","1Ex_eq92",
		"1Be_2k7e","1Ex_exvr",
		"1Be_2gm6","1Ve_7n23",
		"1Be_2gm6","1Ve_85il"};
	
	public static void main(String[] args) {
		testOne();
//		testAll();
//		testAllPairs();
	}

	private static void testAllPairs(){
		String fileExt = ".epml";
		
		//Load EPCs
//		List<Graph> allEPCs = EPCModelParser.readModels(allEPCsFile, false);
		
		//Merge all
		for (int i = 0; i < modelPairs.length; i +=2){
			
			Graph.cleanGraphIDs();
			
			Graph g1 = EPCModelParser.readModels("models/AllEPCs/" + modelPairs[i] + fileExt, false).get(0);
			g1.removeEmptyNodes();
			g1.reorganizeIDs();
			
			Graph g2 = EPCModelParser.readModels("models/AllEPCs/" + modelPairs[i+1] + fileExt, false).get(0);
			g2.removeEmptyNodes();
			g2.reorganizeIDs();
			
			g1.addLabelsToUnNamedEdges();
			g2.addLabelsToUnNamedEdges();

			Graph merged = new MergeModels().mergeModels(g1, g2);
//				int[] gwInf = merged.getNrOfConfigGWs(); 
//				System.out.println(g1.name+"\t"+g1.getVertices().size()+"\t"+g2.name+"\t"+g2.getVertices().size()+"\t"+
//						merged.getVertices().size()+"\t"+gwInf[0]+"\t"+gwInf[1]+"\t"+gwInf[2]+"\t"+gwInf[3]+"\t"+merged.mergetime+"\t"+merged.cleanTime);

			//				EPCModelParser.writeModel(result_prefix + g1.name +"_"+g2.name+"_merged.epml", merged);
			System.out.println(g1.name+"\t"+g1.getVertices().size()+"\t"+g2.name+"\t"+g2.getVertices().size()+"\t"+
					merged.mergetime+"\t"+merged.beforeReduction+"\t"+((double)merged.beforeReduction/(double)(g1.getVertices().size()+g2.getVertices().size()))
					+"\t"+merged.getVertices().size()+"\t"+((double)merged.getVertices().size()/(double)(g1.getVertices().size()+g2.getVertices().size())));

		}
	}
	
	private static void testAll(){
		String fileExt = ".epml";
		
		//Load EPCs
//		List<Graph> allEPCs = EPCModelParser.readModels(allEPCsFile, false);
		
		//Merge all
		for (int i = 0; i < models.length - 1; i++){
			for (int j = i + 1; j < models.length; j++){
				
				Graph.cleanGraphIDs();
				
				Graph g1 = EPCModelParser.readModels("models/AllEPCs/" + models[i] + fileExt, false).get(0);
				g1.removeEmptyNodes();
				g1.reorganizeIDs();
				
				Graph g2 = EPCModelParser.readModels("models/AllEPCs/" + models[j] + fileExt, false).get(0);
				g2.removeEmptyNodes();
				g2.reorganizeIDs();
				
				g1.addLabelsToUnNamedEdges();
				g2.addLabelsToUnNamedEdges();

				Graph merged = new MergeModels().mergeModels(g1, g2);
//				int[] gwInf = merged.getNrOfConfigGWs(); 
//				System.out.println(g1.name+"\t"+g1.getVertices().size()+"\t"+g2.name+"\t"+g2.getVertices().size()+"\t"+
//						merged.getVertices().size()+"\t"+gwInf[0]+"\t"+gwInf[1]+"\t"+gwInf[2]+"\t"+gwInf[3]+"\t"+merged.mergetime+"\t"+merged.cleanTime);

				//				EPCModelParser.writeModel(result_prefix + g1.name +"_"+g2.name+"_merged.epml", merged);
				System.out.println(g1.name+"\t"+g1.getVertices().size()+"\t"+g2.name+"\t"+g2.getVertices().size()+"\t"+
						merged.mergetime+"\t"+merged.beforeReduction+"\t"+((double)merged.beforeReduction/(double)(g1.getVertices().size()+g2.getVertices().size()))
						+"\t"+merged.getVertices().size()+"\t"+((double)merged.getVertices().size()/(double)(g1.getVertices().size()+g2.getVertices().size())));

			}
		}
	}
	
	private static void testOne(){
		String fileExt = ".epml";
		
//		"1Pr_fbh-","1Pr_htc-",
		String s1 = "1An_klol";
		String s2 = "1An_l1y8";

		
		
		Graph g1 = EPCModelParser.readModels("models/AllEPCs/" + s1 + fileExt, false).get(0);
		g1.removeEmptyNodes();
		g1.reorganizeIDs();
		
		Graph g2 = EPCModelParser.readModels("models/AllEPCs/" + s2 + fileExt, false).get(0);
		g2.removeEmptyNodes();
		g2.reorganizeIDs();
		
		g1.addLabelsToUnNamedEdges();
		g2.addLabelsToUnNamedEdges();

		int g1Size = g1.getVertices().size();
		int g2Size = g2.getVertices().size();
			
		Graph merged = new MergeModels().mergeModels(g1, g2);
		int[] gwInf = merged.getNrOfConfigGWs(); 
		System.out.println(g1.name+"\t"+g1Size+"\t"+g2.name+"\t"+g2Size+"\t"+
				merged.getVertices().size()+"\t"+gwInf[0]+"\t"+gwInf[1]+"\t"+gwInf[2]+"\t"+gwInf[3]+"\t"+merged.mergetime+"\t"+merged.cleanTime);
		EPCModelParser.writeModel(result_prefix + g1.name +"_"+g2.name+"_merged_reina.epml", merged);
		
	}
}
