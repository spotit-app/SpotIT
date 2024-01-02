export interface WriteSoftSkill {
  skillLevel: number;
  softSkillName: string;
}

export interface ReadSoftSkill {
  id: number;
  skillLevel: number;
  softSkillName: string;
}

export interface SoftSkillsFormType {
  softSkillName: string;
  customSoftSkillName: string;
  softSkillLevel: number;
}

export interface ReadSoftSkillName {
  id: number;
  name: string;
}

export interface WriteSoftSkillName {
  name: string;
}

export interface ReadSocial {
  id: number;
  name: string;
  socialUrl: string;
}

export interface WriteSocial {
  name: string;
  socialUrl: string;
}

export interface ReadEducation {
  id: number;
  schoolName: string;
  faculty: string;
  startDate: string;
  endDate: string;
  educationLevel: string;
}

export interface WriteEducation {
  schoolName: string;
  faculty: string;
  startDate: string;
  endDate: string;
  educationLevel: string;
}

export interface EducationFormType {
  schoolName: string;
  faculty: string;
  startDate: string;
  endDate: string;
  educationLevel: string;
  customEducationLevel: string;
}

export interface ReadEducationLevel {
  id: number;
  name: string;
}

export interface WriteEducationLevel {
  name: string;
}

export interface ReadExperience {
  id: number;
  companyName: string;
  position: string;
  startDate: string;
  endDate: string | null;
}

export interface WriteExperience {
  companyName: string;
  position: string;
  startDate: string;
  endDate: string | null;
}

export interface ReadTechSkill {
  id: number;
  skillLevel: number;
  techSkillName: string;
  logoUrl?: string;
}

export interface WriteTechSkill {
  skillLevel: number;
  techSkillName: string;
}

export interface TechSkillsFormType {
  techSkillName: string;
  customTechSkillName: string;
  techSkillLevel: number;
}

export interface ReadTechSkillName {
  id: number;
  name: string;
  logoUrl?: string;
}

export interface WriteTechSkillName {
  name: string;
  logo: File;
}

export interface ReadForeignLanguage {
  id: number;
  languageLevel: number;
  foreignLanguageName: string;
  flagUrl: string;
}

export interface WriteForeignLanguage {
  languageLevel: string;
  foreignLanguageNameId: number;
}

export interface ReadForeignLanguageName {
  id: number;
  name: string;
  flagUrl: string;
}

export interface WriteForeignLanguageName {
  name: string;
  flag: File;
}

export interface ReadInterest {
  id: number;
  name: string;
}

export interface WriteInterest {
  name: string;
}

export interface ReadCourse {
  id: number;
  name: string;
  finishDate: string;
}

export interface WriteCourse {
  name: string;
  finishDate: string;
}

export interface ReadProject {
  id: number;
  name: string;
  description: string;
  projectUrl: string;
}

export interface WriteProject {
  name: string;
  description: string;
  projectUrl: string;
}

export interface ReadUser {
  id: number;
  auth0Id: string;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  profilePictureUrl: string;
  position: string;
  description: string;
  cvClause: string;
}

export interface CreateUser {
  auth0Id: string;
  email: string;
  profilePicture: string;
}

export interface DeleteResponse {
  id: number;
}
