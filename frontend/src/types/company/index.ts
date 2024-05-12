import {
  ReadForeignLanguageName,
  ReadPortfolioPageDto,
  ReadSoftSkillName,
  ReadTechSkillName
} from '../profile';

export interface CompanyForm {
  name: string;
  nip: string;
  regon: string;
  websiteUrl: string;
  country: string;
  zipCode: string;
  city: string;
  street: string;
}

export interface WriteCompany {
  name: string;
  nip: string;
  regon: string;
  websiteUrl: string;
  address: WriteCompanyAddress;
}

export interface UpdateCompany {
  writeDto: WriteCompany;
  profilePicture: File | string;
}

export interface ReadCompany {
  id: string;
  name: string;
  nip: string;
  regon: string;
  websiteUrl: string;
  logoUrl: string;
  address: ReadCompanyAddress;
}

interface ReadCompanyAddress {
  id: string;
  country: string;
  zipCode: string;
  city: string;
  street: string;
}

interface WriteCompanyAddress {
  country: string;
  zipCode: string;
  city: string;
  street: string;
}

export interface ReadJobOffer {
  id: number;
  name: string;
  position: string;
  description: string;
  minSalary: number;
  maxSalary: number;
  benefits: string;
  dueDate: string;
  workExperienceName: string;
  techSkillNames: ReadTechSkillName[];
  softSkillNames: ReadSoftSkillName[];
  foreignLanguageNames: ReadForeignLanguageName[];
  workModes: ReadWorkMode[];
  company: ReadCompany;
}

export interface WriteJobOffer {
  name: string;
  position: string;
  description: string;
  minSalary: number;
  maxSalary?: number;
  benefits: string;
  dueDate: string;
  workExperienceId: number;
  techSkillNames: string[];
  softSkillNames: string[];
  foreignLanguageNamesIds: number[];
  workModesIds: number[];
}

export interface JobOfferPage {
  totalPages: number;
  totalElements: number;
  pageable: {
    pageNumber: number;
    pageSize: number;
    offset: number;
    sort: {
      sorted: boolean;
      empty: boolean;
      unsorted: boolean;
    };
    paged: boolean;
    unpaged: boolean;
  };
  first: boolean;
  last: boolean;
  size: number;
  content: ReadJobOffer[];
  number: number;
  sort: {
    sorted: boolean;
    empty: boolean;
    unsorted: boolean;
  };
  numberOfElements: number;
  empty: boolean;
}

export interface JobOffersQueryParams {
  page: number;
  techSkillNameIds: string[];
  foreignLanguageNameIds: string[];
  experienceNameIds: string[];
  workModeIds: string[];
}

export interface PaginationQueryParams {
  page: number;
}

export interface JobApplicationsQueryParams {
  page: number;
  status: string;
}

export interface ReadWorkMode {
  id: number;
  name: string;
}

export interface WriteWorkMode {
  name: string;
}

export interface ReadWorkExperience {
  id: number;
  name: string;
}

export interface WriteWorkExperience {
  name: string;
}

export interface JobApplicationPage {
  totalPages: number;
  totalElements: number;
  pageable: {
    pageNumber: number;
    pageSize: number;
    offset: number;
    sort: {
      sorted: boolean;
      empty: boolean;
      unsorted: boolean;
    };
    paged: boolean;
    unpaged: boolean;
  };
  first: boolean;
  last: boolean;
  size: number;
  content: ReadJobApplication[];
  number: number;
  sort: {
    sorted: boolean;
    empty: boolean;
    unsorted: boolean;
  };
  numberOfElements: number;
  empty: boolean;
}

export interface ReadJobApplication {
  id: number;
  jobOffer: ReadJobOffer;
  portfolio: ReadPortfolioPageDto;
  applicationStatus: string;
}

export interface UpdateApplicationStatus {
  companyId: number;
  jobOfferId: number;
  applicationId: number;
  newStatus: string;
}
