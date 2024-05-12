import { ReadCompany, ReadJobOffer } from 'types/company';
import { render, act, screen, fireEvent } from '@testing-library/react';
import { JobOfferCard } from '.';
import { RouterProvider } from 'providers';

const mockedUsedNavigate = jest.fn();

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockedUsedNavigate
}));

describe('JobOfferCard', () => {
  const company: ReadCompany = {
    id: '1',
    name: 'TestCompanyName',
    logoUrl: 'test.png',
    websiteUrl: 'https://test.com',
    nip: '1234567890',
    regon: '1234567890',
    address: {
      id: '1',
      street: 'TestStreet',
      city: 'TestCity',
      zipCode: '11-111',
      country: 'TestCountry'
    }
  };
  const jobOffer: ReadJobOffer = {
    id: 1,
    name: 'TestName',
    position: 'TestPosition',
    description: 'TestDescription',
    minSalary: 1000,
    maxSalary: 2000,
    benefits: 'TestBenefits',
    dueDate: '2022-12-12',
    workExperienceName: 'TestWorkExperienceName',
    techSkillNames: [{ id: 1, name: 'TestTechSkillName' }],
    softSkillNames: [],
    foreignLanguageNames: [],
    workModes: [],
    company: company
  };

  test('renders correctly', () => {
    act(() =>
      render(
        <RouterProvider>
          <JobOfferCard jobOffer={jobOffer} />
        </RouterProvider>
      )
    );

    expect(screen.getByText('TestName')).toBeInTheDocument();
  });

  test('correctly displays company icon', () => {
    const companyWithoutLogo: ReadCompany = {
      ...company,
      logoUrl: ''
    };
    const testJobOffer: ReadJobOffer = {
      ...jobOffer,
      company: companyWithoutLogo
    };

    act(() =>
      render(
        <RouterProvider>
          <JobOfferCard jobOffer={testJobOffer} />
        </RouterProvider>
      )
    );

    expect(screen.getByTestId('building-icon')).toBeInTheDocument();
  });

  test('correctly navigates to company page', () => {
    act(() =>
      render(
        <RouterProvider>
          <JobOfferCard jobOffer={jobOffer} />
        </RouterProvider>
      )
    );

    const companyButton = screen.getByText('Profil firmy');
    fireEvent.click(companyButton);

    expect(mockedUsedNavigate).toHaveBeenCalled();
  });
});
