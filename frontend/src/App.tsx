import { Route, Routes } from 'react-router-dom';
import { Loading, Navbar } from 'components';
import { Suspense } from 'react';
import {
  Home,
  ProtectedPage,
  PageNotFound,
  PersonalData,
  Courses,
  Education,
  Employees,
  Experience,
  ForeignLanguages,
  Interests,
  Other,
  Projects,
  Socials,
  SoftSkills,
  TechSkills,
  Profile,
  Portfolio,
  WelcomePage,
  Admin,
  AdminEducation,
  AdminForeignLanguages,
  AdminSoftSkills,
  AdminTechSkills,
  MyCompanies,
  MyCompany,
  JobOffer,
  JobOffers,
  Company,
  AdminWorkExperiences,
  AdminWorkModes,
  MyJobApplications,
  JobOfferApplications,
  AdminWelcomePage
} from 'pages';
import 'react-toastify/dist/ReactToastify.css';
import { ToastContainer } from 'react-toastify';

function App() {
  return (
    <>
      <Navbar />
      <ToastContainer />
      <Suspense fallback={<Loading />}>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/portfolio/:portfolioUrl" element={<Portfolio />} />
          <Route path="/pracownicy" element={<Employees />} />
          <Route path="/oferty-pracy" element={<JobOffers />} />
          <Route path="/oferty-pracy/:id" element={<JobOffer />} />
          <Route path="/firmy/:id" element={<Company />} />
          <Route
            path="/profil"
            element={
              <ProtectedPage>
                <Profile />
              </ProtectedPage>
            }
          >
            <Route path="" element={<WelcomePage />} />
            <Route path="dane-osobowe" element={<PersonalData />} />
            <Route path="konta-spolecznosciowe" element={<Socials />} />
            <Route path="edukacja" element={<Education />} />
            <Route path="doswiadczenie" element={<Experience />} />
            <Route path="umiejetnosci-techniczne" element={<TechSkills />} />
            <Route path="umiejetnosci-miekkie" element={<SoftSkills />} />
            <Route path="jezyki-obce" element={<ForeignLanguages />} />
            <Route path="projekty" element={<Projects />} />
            <Route path="zainteresowania" element={<Interests />} />
            <Route path="kursy" element={<Courses />} />
            <Route path="inne" element={<Other />} />
          </Route>
          <Route
            path="/admin"
            element={
              <ProtectedPage adminProtected>
                <Admin />
              </ProtectedPage>
            }
          >
            <Route path="" element={<AdminWelcomePage />} />
            <Route path="edukacja" element={<AdminEducation />} />
            <Route path="umiejetnosci-techniczne" element={<AdminTechSkills />} />
            <Route path="umiejetnosci-miekkie" element={<AdminSoftSkills />} />
            <Route path="jezyki-obce" element={<AdminForeignLanguages />} />
            <Route path="poziom-doswiadczenia" element={<AdminWorkExperiences />} />
            <Route path="tryby-pracy" element={<AdminWorkModes />} />
          </Route>
          <Route
            path="/moje-firmy"
            element={
              <ProtectedPage>
                <MyCompanies />
              </ProtectedPage>
            }
          />
          <Route
            path="/moje-firmy/:id"
            element={
              <ProtectedPage>
                <MyCompany />
              </ProtectedPage>
            }
          />
          <Route
            path="/moje-firmy/:companyId/oferty-pracy/:jobOfferId/aplikacje"
            element={
              <ProtectedPage>
                <JobOfferApplications />
              </ProtectedPage>
            }
          />
          <Route
            path="/moje-aplikacje"
            element={
              <ProtectedPage>
                <MyJobApplications />
              </ProtectedPage>
            }
          />
          <Route path="*" element={<PageNotFound />} />
        </Routes>
      </Suspense>
    </>
  );
}

export default App;
