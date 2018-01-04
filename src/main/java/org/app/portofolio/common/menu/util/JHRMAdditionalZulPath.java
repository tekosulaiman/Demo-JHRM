package org.app.portofolio.common.menu.util;

/**
 * 
 * @author JCodev
 *
 */
public interface JHRMAdditionalZulPath {
	
	/**
	 * 
	 *
	 */
	public interface MasterData {
		
		/**
		 * 
		 *
		 */
		public interface Qualifications{
			/**
			 * 
			 *
			 */
			public interface Skills {
				
				public static final String MAIN_FORM = "/WEB-INF/pages/module_hr/master/qualifications/skills/skillList.zul";
				public static final String ADD_FORM = "/WEB-INF/pages/module_hr/master/qualifications/skills/skillDialog.zul";
				public static final String DETAIL_FORM = "/WEB-INF/pages/module_hr/master/qualifications/skills/skillDetailDialog.zul";
			}
			
			/**
			 * 
			 *
			 */
			public interface Education {
				public static final String MAIN_FORM = "/WEB-INF/pages/module_hr/master/qualifications/educations/educationList.zul";
				public static final String ADD_FORM = "/WEB-INF/pages/module_hr/master/qualifications/educations/educationDialog.zul";
				public static final String DETAIL_FORM = "/WEB-INF/pages/module_hr/master/qualifications/educations/educationDetailDialog.zul";
			}
			
			/**
			 * 
			 *
			 */
			public interface Licenses {
				public static final String MAIN_FORM = "/WEB-INF/pages/module_hr/master/qualifications/licenses/licenseList.zul";
				public static final String ADD_FORM = "/WEB-INF/pages/module_hr/master/qualifications/licenses/licenseDialog.zul";
			}
		}
		
	}
	
	/**
	 * 
	 * @author Sugar Pramana
	 *
	 */
	public interface Recruitment {
		
		/**
		 * 
		 * @author Sugar Pramana
		 *
		 */
		public interface Vacancy {
			
			public static final String DIALOG_FORM = "/WEB-INF/pages/module_hr/transaction/recruitment/vacancy/vacancyDialog.zul";
		}
		
		/**
		 * 
		 */
		public interface Candidate {
			public static final String DIALOG_FORM = "/WEB-INF/pages/module_hr/transaction/recruitment/candidate/candidateDialog.zul";
		}
	}
}
