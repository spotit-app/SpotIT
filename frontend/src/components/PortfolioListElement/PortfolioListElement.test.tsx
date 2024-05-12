import { ReadPortfolioPageDto } from 'types/profile';
import { act, render, screen } from '@testing-library/react';
import { PortfolioListElement } from '.';
import { RouterProvider } from 'providers';

describe('PortfolioListElement', () => {
  test('renders correctly', () => {
    const portfolio: ReadPortfolioPageDto = {
      portfolioUrl: 'TestFirstName_TestLastName',
      userData: {
        firstName: 'TestFirstName',
        lastName: 'TestLastName',
        email: 'test@email.com',
        phoneNumber: '123456789',
        profilePictureUrl: 'test.png',
        position: 'TestPosition',
        description: 'TestDescription',
        cvClause: 'TestClause',
        isOpen: true
      }
    };

    act(() =>
      render(
        <RouterProvider>
          <PortfolioListElement portfolio={portfolio} />
        </RouterProvider>
      )
    );

    expect(screen.getByText('TestFirstName TestLastName')).toBeInTheDocument();
    expect(screen.getByText('TestFirstName_TestLastName')).toBeInTheDocument();
  });
});
